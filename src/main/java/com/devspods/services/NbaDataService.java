package com.devspods.services;

import com.devspods.domain.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.devspods.util.Constants.*;
import static java.lang.Math.abs;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@RequiredArgsConstructor
public class NbaDataService {

    @Value("${services.nba.data.url}")
    private String dataUrl;
    private Map<Integer, Set<Player>> players = new HashMap<>();
    private final RestTemplate template;

    @PostConstruct
    public void init() {
        log.info("Starting to load players...");
        try {
            var response = template.getForEntity(dataUrl, JsonNode.class);
            if (isValidResponse(response)) {
                var values = response.getBody().get(VALUES_FIELD);
                players = new HashMap<>(values.size());
                for (var node : values) {
                    var player = convertToPlayer(node);
                    if (players.containsKey(player.getHeight())) {
                        players.get(player.getHeight()).add(player);
                    } else {
                        players.put(player.getHeight(), new HashSet<>(Set.of(player)));
                    }
                }
                log.info("{} players loaded.", values.size());
            } else {
                log.warn("Error reading response: {}", response);
            }
        } catch (final Exception exception) {
            log.warn("Error loading players: {}", exception.getMessage());
        }
    }

    private boolean isValidResponse(final ResponseEntity<JsonNode> response) {
        return response.getStatusCode() == OK && response.getBody() != null && response.getBody().hasNonNull(VALUES_FIELD);
    }

    public Set<Pair> getPairs(final int heightSum) {
        Set<Integer> visited = new HashSet<>();
        Set<Pair> pairs = new HashSet<>();
        for (var entry : players.entrySet()) {
            var key = entry.getKey();
            var delta = abs(heightSum - key);
            if (players.containsKey(delta) && !visited.contains(key) && !visited.contains(delta) && key != delta) {
                pairs.addAll(merge(entry.getValue(), players.get(delta)));
            }
            visited.add(key);
        }
        return pairs;
    }

    private Set<Pair> merge(final Set<Player> firstSet, final Set<Player> secondSet) {
        Set<Pair> pairs = new HashSet<>();
        firstSet.forEach(pa -> secondSet.forEach(pb -> pairs.add(new Pair(pa, pb))));
        return pairs;
    }

    private Player convertToPlayer(final JsonNode node) {
        return Player.builder()
                     .firstName(node.get(FIRST_NAME_FIELD).asText())
                     .lastName(node.get(LAST_NAME_FIELD).asText())
                     .height(node.get(HEIGHT_IN_INCHES_FIELD).asInt())
                     .build();
    }
}
