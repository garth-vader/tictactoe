// package tictactoe;

// import org.junit.Test;
// import static org.junit.Assert.assertEquals;

// public class TictactoeTest {

// @Test
// public void test_getStatusWins() {
// String result = Main.result("XXXOO O ".toCharArray());
// assertEquals("X wins", result);
// result = Main.result("XOXOXOXXO".toCharArray());
// assertEquals("X wins", result);
// result = Main.result("XOOOXOXXO".toCharArray());
// assertEquals("O wins", result);
// }

// @Test
// public void test_getStatusDraw() {
// String result = Main.result("XOXOOXXXO".toCharArray());
// assertEquals("Draw", result);
// }

// @Test
// public void test_getStatusNotFin() {
// String result = Main.result("XO OOX X ".toCharArray());
// assertEquals("Game not finished", result);
// result = Main.result(" ".toCharArray());
// assertEquals("Game not finished", result);
// }

// @Test
// public void test_getStatusImpossible() {
// String result = Main.result("XO XO XOX".toCharArray());
// assertEquals("Impossible", result);
// result = Main.result(" O X X X".toCharArray());
// assertEquals("Impossible", result);
// result = Main.result(" OOOO X X".toCharArray());
// assertEquals("Impossible", result);

// }
// }