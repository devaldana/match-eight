package com.devspods;

import com.devspods.services.NbaDataService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private final NbaDataService nbaDataService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(final String... args) {
        var scanner = new Scanner(System.in);
        var exit = false;
        while (!exit && "default".equals(activeProfile)) {
            System.out.print("\nEnter the value: ");
            var value = scanner.nextInt();
            var pairs = nbaDataService.getPairs(value);
            System.out.println("\n### " + pairs.size() + " pairs found ###");
            System.out.println("----------------------------------------------------");
            pairs.forEach(System.out::print);
            exit = value == 0;
        }
    }
}