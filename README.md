### Quick start
This guide assumes that you have `maven` and `Java 11` installed.
Please go to the root project folder and execute `mvn spring-boot:run` and input a value to search for, lets say `140`
```
Enter the value: 140
```

Next, you will find the results:
```
### 2 pairs found ###
----------------------------------------------------
69" - Nate Robinson             71" - Chucky Atkins
69" - Nate Robinson             71" - Speedy Claxton
```

To finish the application input `0`:
```
Enter the value: 0
```

#### Tests
To execute the tests:
```
mvn clean test
```