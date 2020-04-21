package ch.m1m.junit5;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// AssertJ  over hamcrest
//

@DisplayName("JUnit 5 Example")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JUnit5ExampleTest {

    @BeforeAll
    static void beforeAll() {
        //System.out.println("Before all test methods");
    }

    @BeforeEach
    void beforeEach() {
        //System.out.println("Before each test method");
    }

    @AfterEach
    void afterEach() {
        //System.out.println("After each test method");
    }

    @AfterAll
    static void afterAll() {
        //System.out.println("After all test methods");
    }

    @Test
    @Order(1)
    @DisplayName("this is my test 1 xxx")
    public void givenJUnit5test_whenExecuting_thenItWorks() {
        System.out.println("test xxx running...");
        // GIVEN

        // WHEN

        // THEN
        if (false) {
            fail("this failed");
        }
        assertTrue(true);
    }

    @Test
    @Order(2)
    public void check_throw_IllegalArgumentException(TestInfo info) {
        assertThrows(IllegalArgumentException.class, () -> {
            if (true) {
                throw new IllegalArgumentException("value is bigger than 10");
            }
        });
    }

    @Test
    @Disabled
    public void checkMoreInputValues() {

        boolean rc = checkArgumentsLower_100(10);

        assertEquals(true, rc);
    }

    // @ParameterizedTest
    // @CvsSource
    // @String
    private boolean checkArgumentsLower_100(int num) {
        if (num < 100) {
            return true;
        }
        return false;
    }
}
