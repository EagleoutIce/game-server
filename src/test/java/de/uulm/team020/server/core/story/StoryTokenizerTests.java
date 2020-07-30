package de.uulm.team020.server.core.story;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.server.core.dummies.story.helper.StoryTokenizer;
import de.uulm.team020.validation.GameDataGson;

/**
 * Test the implementation of the tokenizer
 * 
 * @author Florian Sihler
 * @version 1.0, 04/16/2020
 */
@Tag("Tokenizer")
@TestMethodOrder(OrderAnnotation.class)
public class StoryTokenizerTests {

    public static Stream<Arguments> generate_validateTokenizer() throws IOException {
        return Stream.of(Arguments.arguments("", new String[] {}),
                Arguments.arguments("Hallo", new String[] { "Hallo" }),
                Arguments.arguments("(Hallo)Welt!", new String[] { "(Hallo)Welt!" }),
                Arguments.arguments("Huhu Buhu Wuhu", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments("Huhu Bu\\\"hu Wuhu", new String[] { "Huhu", "Bu\\\"hu", "Wuhu" }),
                Arguments.arguments(
                        "LongerSuperLongLongLongLongToken ThisIsSuchALongTokenThatIsEvenLongerThanExpectedIsItNotGreat?",
                        new String[] { "LongerSuperLongLongLongLongToken",
                                "ThisIsSuchALongTokenThatIsEvenLongerThanExpectedIsItNotGreat?" }),
                Arguments.arguments(
                        "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z ab cd ef gh ij kl mn op qr st uv wx yz ? !",
                        "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z ab cd ef gh ij kl mn op qr st uv wx yz ? !"
                                .split("\\s")),
                Arguments.arguments("Huhu         Buhu Wuhu", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments("Huhu         Buhu      Wuhu", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments("Huhu Buhu Wuhu      ", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments("Huhu         Buhu Wuhu      ", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments("      Huhu Buhu Wuhu", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments("Huhu Buhu Wuhu ", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments(" Huhu  Buhu  Wuhu  ", new String[] { "Huhu", "Buhu", "Wuhu" }),
                Arguments.arguments("Hallo \"Welt wie\" geht es dir?",
                        new String[] { "Hallo", "Welt wie", "geht", "es", "dir?" }),
                Arguments.arguments("Hallo \" Welt wie\" gehts?", new String[] { "Hallo", " Welt wie", "gehts?" }),
                Arguments.arguments("Hallo \"Welt wie \" gehts?", new String[] { "Hallo", "Welt wie ", "gehts?" }),
                Arguments.arguments("Hallo \"Welt   wie \" gehts?", new String[] { "Hallo", "Welt   wie ", "gehts?" }),
                Arguments.arguments("\"Hallo \" \"Welt \\\"wie\\\" \" gehts?",
                        new String[] { "Hallo ", "Welt \"wie\" ", "gehts?" }),
                Arguments.arguments("\"A \" \" B\" \"C \"d", new String[] { "A ", " B", "C d" }),
                Arguments.arguments("\" \" \" B\" \"C \"d", new String[] { " ", " B", "C d" }),
                Arguments.arguments("\" \" \" B\" d\"C \"d", new String[] { " ", " B", "d\"C \"d" }),
                Arguments.arguments("\" \" \" B\" d\"C\\\"Huhu\\\" \"d",
                        new String[] { " ", " B", "d\"C\\\"Huhu\\\" \"d" }),
                Arguments.arguments("\"     \" \" B\" \"C \"d", new String[] { "     ", " B", "C d" }),
                Arguments.arguments("\"\" \" B\" \"C \"d", new String[] { "", " B", "C d" }),
                Arguments.arguments(GameDataGson.loadInternalJson("simple/exampleLine.story"), new String[] {
                        "CONFIG_INJECT", "characters", "raw-json",
                        "[{\"characterId\":\"20ff071e-e344-449f-a8c5-dd679bc92a68\",\"name\":\"James Bond\",\"description\":\"Bester Geheimagent aller Zeiten mit 00-Status.\",\"gender\":\"DIVERSE\",\"features\":[\"SPRYNESS\",\"TOUGHNESS\",\"ROBUST_STOMACH\",\"LUCKY_DEVIL\",\"TRADECRAFT\"]},{\"characterId\":\"65cc7959-aab2-4d81-8877-e5eb74fe2fdf\",\"name\":\"Meister Yoda\",\"description\":\"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.\",\"gender\":null,\"features\":[\"LUCKY_DEVIL\",\"OBSERVATION\",\"TOUGHNESS\"]},{\"characterId\":\"220b7aad-429c-4f18-a019-c24a32d4a628\",\"name\":\"Tante Gertrude\",\"description\":\"Nach wie vor die beste Tante, die man sich wünschen kann.\",\"gender\":\"FEMALE\",\"features\":[\"NIMBLENESS\",\"BABYSITTER\",\"TOUGHNESS\"]},{\"characterId\":\"24fee6e2-a321-4161-8634-833b7a2c472d\",\"name\":\"The legendary Gustav\",\"description\":\"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.\",\"gender\":null,\"features\":[\"AGILITY\",\"LUCKY_DEVIL\",\"BANG_AND_BURN\",\"TRADECRAFT\",\"OBSERVATION\"]},{\"characterId\":\"a12dc0b0-bae9-4b8a-8e51-2d51e13ab01b\",\"name\":\"Hans Peter Otto\",\"description\":\"Auch Hans Otto, oder Otto-Normal genannt.\",\"gender\":\"MALE\",\"features\":[\"ROBUST_STOMACH\",\"FLAPS_AND_SEALS\"]},{\"characterId\":\"4f7664f4-20cc-4a97-a13b-574a63900595\",\"name\":\"Ein Wischmob\",\"description\":\"Wieso sollte der nicht mitspielen dürfen?\",\"gender\":null,\"features\":[\"JINX\",\"SPRYNESS\",\"HONEY_TRAP\"]},{\"characterId\":\"d9beaf7e-24de-43b1-bf08-7ba6d3fd6dbb\",\"name\":\"Zackiger Zacharias\",\"description\":\"Langsamer, als die Polizei erlaubt.\",\"gender\":\"DIVERSE\",\"features\":[\"PONDEROUSNESS\",\"ROBUST_STOMACH\"]},{\"characterId\":\"c131e8ab-5b07-4cd2-95a8-a9a680112cfa\",\"name\":\"Schleim B. Olzen\",\"description\":null,\"gender\":\"MALE\",\"features\":[\"LUCKY_DEVIL\",\"NIMBLENESS\",\"TRADECRAFT\"]},{\"characterId\":\"47c44e32-827d-4ff5-85ca-151bc04b1060\",\"name\":\"Spröder Senf\",\"description\":\"Alle Macht dem Senf\",\"gender\":null,\"features\":[\"SPRYNESS\",\"CONSTANT_CLAMMY_CLOTHES\",\"OBSERVATION\"]},{\"characterId\":\"6cef511a-49c3-4c0f-b6d7-4b8642b0356d\",\"name\":\"Petterson\",\"description\":\"Den Findus keiner.\",\"gender\":null,\"features\":[\"HONEY_TRAP\",\"BABYSITTER\",\"FLAPS_AND_SEALS\"]},{\"characterId\":\"7c01f4ee-0a94-4c79-8d93-860267fbf97e\",\"name\":\"Mister X\",\"description\":\"Wohin könnte er nur gehen?\",\"gender\":\"MALE\",\"features\":[\"AGILITY\",\"LUCKY_DEVIL\"]},{\"characterId\":\"f9c0ca2f-e1a6-4b35-afc1-74652ada2ca5\",\"name\":\"Mister Y\",\"description\":\"Leider als Einzelkind aufgewachsen. Sowas prägt.\",\"gender\":\"MALE\",\"features\":[\"LUCKY_DEVIL\"]},{\"characterId\":\"c81c17e9-00c3-421f-ac06-0d74f6215dc8\",\"name\":\"Misses Y\",\"description\":\"Ist eigentlich nur für die Gleichberechtigung hier.\",\"gender\":\"FEMALE\",\"features\":[\"OBSERVATION\",\"TOUGHNESS\"]},{\"characterId\":\"c15c6cea-9384-479c-baec-0b9add3a7747\",\"name\":\"Austauschbarer Agent Dieter 42\",\"description\":\"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.\",\"gender\":\"DIVERSE\",\"features\":[\"HONEY_TRAP\",\"LUCKY_DEVIL\"]},{\"characterId\":\"f9ed4e03-57be-48ff-aed3-5c78da11016b\",\"name\":\"Saphira\",\"description\":\"Natürlich ist sie im Pool... Es ist immerhin \\\"Saphira\\\", die beste!\",\"gender\":\"FEMALE\",\"features\":[\"AGILITY\",\"LUCKY_DEVIL\",\"BANG_AND_BURN\",\"TOUGHNESS\"]}]" }));
    }

    @ParameterizedTest
    @Tag("Core")
    @Order(1)
    @DisplayName("[StoryTokenizer] Validate Tokenizer")
    @MethodSource("generate_validateTokenizer")
    public void test_validateTokenizer(String line, String[] expected) {
        StoryTokenizer tokenizer = new StoryTokenizer();
        String[] got = tokenizer.tokenize(line);
        Assertions.assertArrayEquals(expected, got, "Should be as expected with '" + line + "' got: '"
                + Arrays.toString(got) + "' when expected: '" + Arrays.toString(expected) + "'.");
    }

    public static Stream<Arguments> generate_validateTokenizerEscape() throws IOException {
        return Stream.of(Arguments.arguments("", ""), Arguments.arguments("Hallo", "Hallo"),
                Arguments.arguments("Hallo Welt", "\"Hallo Welt\""),
                Arguments.arguments("\"Hallo Welt\"", "\"Hallo Welt\""),
                Arguments.arguments("Dies ist unsere\"Hallo Welt\"", "\"Dies ist unsere\\\"Hallo Welt\\\"\""));
    }

    @ParameterizedTest
    @Tag("Core")
    @Order(2)
    @DisplayName("[StoryTokenizer] Validate Tokenizer-Escape")
    @MethodSource("generate_validateTokenizerEscape")
    public void test_validateTokenizerEscape(String line, String expected) {
        Assertions.assertEquals(expected, StoryTokenizer.escape(line), "Should be as expected");
    }

}