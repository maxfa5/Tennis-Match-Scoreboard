# Тестирование с JUnit 5

Этот проект использует JUnit 5 для написания тестов. Здесь представлены различные типы тестов и примеры их использования.

## Структура тестов

```
src/test/java/org/project/
├── controller/          # Тесты контроллеров
├── service/            # Тесты сервисов
├── repository/         # Тесты репозиториев
└── resources/          # Тестовые ресурсы
    └── application-test.properties
```

## Типы тестов

### 1. Unit-тесты (MatchScoreCalculationServiceTest.java)
- Тестируют отдельные методы классов
- Используют Mockito для мокирования зависимостей
- Быстрые и изолированные

```java
@ExtendWith(MockitoExtension.class)
class MatchScoreCalculationServiceTest {
    @Mock
    private OngoingMatchesService ongoingMatchesService;
    
    @InjectMocks
    private MatchScoreCalculationService service;
    
    @Test
    void shouldIncrementPlayer1Score() {
        // Given
        IncrementScoreDTO dto = new IncrementScoreDTO();
        dto.setMatchId(matchId);
        dto.setAddToPLayer1(1);
        
        // When
        boolean result = service.IncrementScore(dto);
        
        // Then
        assertFalse(result);
    }
}
```

### 2. Интеграционные тесты (MatchScoreControllerTest.java)
- Тестируют взаимодействие между компонентами
- Используют @WebMvcTest для тестирования контроллеров
- Проверяют HTTP запросы и ответы

```java
@WebMvcTest(MatchScoreController.class)
class MatchScoreControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldReturnHomePage() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"));
    }
}
```

### 3. Тесты репозитория (FinishedMatchRepositoryTest.java)
- Тестируют работу с базой данных
- Используют @DataJpaTest для настройки тестовой БД
- Проверяют SQL запросы и маппинг

```java
@DataJpaTest
class FinishedMatchRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void shouldFindMatchesByPlayerName() {
        // Given
        Player player = new Player("John");
        entityManager.persistAndFlush(player);
        
        // When
        List<FinishedMatch> matches = repository.findByPlayerNameContaining("John", PageRequest.of(0, 10));
        
        // Then
        assertEquals(1, matches.size());
    }
}
```

### 4. Параметризованные тесты (ParameterizedServiceTest.java)
- Тестируют один метод с разными наборами данных
- Используют @ParameterizedTest с различными провайдерами

```java
@ParameterizedTest
@CsvSource({
    "John, Jane, John",
    "Jane, Bob, Jane"
})
void shouldDetermineMatchWinner(String player1, String player2, String expectedWinner) {
    String winner = determineWinner(player1, player2);
    assertEquals(expectedWinner, winner);
}
```

## Запуск тестов

### Запуск всех тестов
```bash
./gradlew test
```

### Запуск конкретного теста
```bash
./gradlew test --tests MatchScoreCalculationServiceTest
```

### Запуск тестов с подробным выводом
```bash
./gradlew test --info
```

### Запуск тестов в IDE
- IntelliJ IDEA: Правый клик на папке test → Run Tests
- Eclipse: Правый клик на папке test → Run As → JUnit Test

## Аннотации JUnit 5

### Основные аннотации
- `@Test` - помечает метод как тест
- `@DisplayName` - задает читаемое имя теста
- `@BeforeEach` - выполняется перед каждым тестом
- `@AfterEach` - выполняется после каждого теста
- `@BeforeAll` - выполняется один раз перед всеми тестами класса
- `@AfterAll` - выполняется один раз после всех тестов класса

### Аннотации для параметризованных тестов
- `@ParameterizedTest` - параметризованный тест
- `@ValueSource` - простые значения
- `@CsvSource` - CSV данные
- `@MethodSource` - данные из метода
- `@EnumSource` - значения из enum

### Аннотации для группировки
- `@Tag` - группировка тестов
- `@Nested` - вложенные тестовые классы

## Mockito

### Основные методы
```java
// Мокирование
when(mock.method()).thenReturn(value);
when(mock.method(any())).thenReturn(value);

// Проверка вызовов
verify(mock).method();
verify(mock, times(2)).method();
verify(mock, never()).method();

// Аргументы
ArgumentCaptor<Type> captor = ArgumentCaptor.forClass(Type.class);
verify(mock).method(captor.capture());
assertEquals(expectedValue, captor.getValue());
```

## Лучшие практики

### 1. Именование тестов
```java
@Test
@DisplayName("Должен увеличить счет первого игрока при валидном DTO")
void shouldIncrementPlayer1Score_WhenValidDTO() {
    // тест
}
```

### 2. Структура теста (AAA Pattern)
```java
@Test
void testMethod() {
    // Arrange (Given) - подготовка данных
    String input = "test";
    
    // Act (When) - выполнение действия
    String result = service.process(input);
    
    // Assert (Then) - проверка результата
    assertEquals("expected", result);
}
```

### 3. Изоляция тестов
- Каждый тест должен быть независимым
- Используйте @BeforeEach для настройки
- Очищайте состояние после тестов

### 4. Покрытие тестами
- Unit-тесты: 80-90% покрытия
- Интеграционные тесты: критичные сценарии
- E2E тесты: основные пользовательские сценарии

## Конфигурация

### application-test.properties
```properties
# Тестовая база данных
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### build.gradle.kts
```kotlin
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-junit-jupiter")
}
```

## Отладка тестов

### В IDE
- Установите breakpoints в тестах
- Используйте Debug режим для запуска тестов

### Логирование
```java
@Test
void testWithLogging() {
    System.out.println("Тестовые данные: " + testData);
    // или используйте логгер
    log.info("Выполняется тест с данными: {}", testData);
}
```

### H2 Console (для тестов БД)
```properties
spring.h2.console.enabled=true
```
Доступ: http://localhost:8080/h2-console 