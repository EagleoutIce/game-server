package de.uulm.team020.server.addons.random;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.exceptions.PointParseException;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryMethod;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryArgumentException;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryRandomArgumentException;

/**
 * This controller was added after the main server-control was established but
 * aims to handle <i>all</i> access to random functions performed by any
 * non-story server routine.
 * <p>
 * Why? This controller may be used by the Story-Component to inject the outcome
 * of random-routines in a queue-based manner.
 * <p>
 * How to use? For any {@link RandomOperation} combined with a given argument
 * there may be a value injected (probably boolean or integer). Whenever the
 * server needs a radom value it will ask its random controller instance to
 * provide one. If there is a value injected the Controller will use it and
 * remove the value from the given queue. If there is no value the controller
 * will fall back to the default random generation -- in both cases the
 * {@link StoryAuthor} will write the decision to the random section.
 * <p>
 * This class may be refactored multiple times
 * 
 * <pre>
 *CONFIG_INJECT rnd "Operation_Success" "James Bond:true,true,false,true,false,true"
 *CONFIG_INJECT rnd "NPC_Movement" "Saphira:up,left,right,up,up,retire,up,moledie"
 * ...
 * </pre>
 * 
 * The names will refer to the characters that execute the operation -- if there
 * is no character it will fall back to the player or an generic name.
 * 
 * @author Florian Sihler
 * @version 1.0, 06/07/2020
 * 
 * @since 1.1
 */
public class RandomController {

    private static final String AND_OP_TXT = " and operation: ";
    private static final String TO_TXT = " to: ";
    private static final String ENFORCE_TXT = "enforce";

    private static IMagpie magpie = Magpie.createMagpieSafe("Random");

    /** The global token of the controller */
    public static final String GLOBAL = "global";
    /** The true token of the controller */
    public static final String TRUE = "true";

    private final Map<String, RandomControllerSettings> settings;
    private final StoryAuthor author;

    /**
     * Build the random controller, please note, that this won't perform any
     * injection by itself
     * 
     * @param author The author that should be used to log the choices
     */
    public RandomController(final StoryAuthor author) {
        super();
        this.settings = new HashMap<>();
        this.author = author;
    }

    /**
     * This will request a toss by the Random-Controller which will delegate the
     * call to {@link RandomHelper#rndInt(int,int)} by default. This behaviour is
     * altered if and only if there is a injection matching the given name and
     * operation. In this case the controller will pop the given result and return
     * it so it may be used transparent from a real toss.
     * <p>
     * Any choice made, even if it was injected, will be written to the supplied
     * {@link StoryAuthor} (see {@link #RandomController(StoryAuthor)}).
     * <p>
     * It is up to you to supply only options that want a range result, this may
     * change in the future
     * 
     * @param name           The name of the target requesting the toss
     * @param inclusiveLower The lower bound of the range (inclusive)
     * @param exclusiveUpper The upper bound of the range (exclusive)
     * @param operation      The operation a toss is desired for, will be used to
     *                       distinguish injections
     * 
     * @return Integer in the given range or injected
     */
    public int requestRange(String name, int inclusiveLower, int exclusiveUpper, RandomOperation operation) {
        RandomControllerSettings nameSettings = settings.get(name);
        Queue<String> data;

        int result;
        // if there is an injection for this name, it has data injected for the
        // operation and the data is present
        if (nameSettings != null && (data = nameSettings.get(operation)) != null && !data.isEmpty()) {
            result = useInjectedRange(name, inclusiveLower, exclusiveUpper, operation, data);
        } else {
            result = RandomHelper.rndInt(inclusiveLower, exclusiveUpper);
        }

        author.storeRandomResult(name, operation, Integer.toString(result));
        return result;
    }

    private int useInjectedRange(String name, int inclusiveLower, int exclusiveUpper, RandomOperation operation,
            Queue<String> data) {
        String intStr = data.remove();
        int result;
        // remove an entry, we do not care if it is not a sensible boolean value as this
        // might be
        // checked only if decoded by the Inject-Routine
        try {
            result = Integer.parseInt(intStr);
        } catch (NumberFormatException ex) {
            throw new StoryRandomArgumentException("The operation: " + operation + " request for: " + name
                    + " wants an integer value for the range-target but you supplied: '" + intStr
                    + "' wich could not be parsed.");
        }

        if (result < inclusiveLower || result >= exclusiveUpper) {
            throw new StoryRandomArgumentException("You wanted the value: " + result + " for operation: " + operation
                    + " and Name: " + name + " but the lower-bound (inclusive): " + inclusiveLower
                    + " or the upper-bound (exclusive): " + exclusiveUpper + " forbids it!.");
        }
        magpie.writeWarning("Injected random result for: " + name + AND_OP_TXT + operation + TO_TXT + result
                + ". Original from: " + inclusiveLower + TO_TXT + exclusiveUpper + ".", ENFORCE_TXT);
        return result;
    }

    /**
     * This will request a toss by the Random-Controller which will delegate the
     * call to {@link RandomHelper#flip(double)} by default. This behaviour is
     * altered if and only if there is a injection matching the given name and
     * operation. In this case the controller will pop the given result and return
     * it so it may be used transparent from a real toss.
     * <p>
     * Any choice made, even if it was injected, will be written to the supplied
     * {@link StoryAuthor} (see {@link #RandomController(StoryAuthor)}).
     * <p>
     * It is up to you to supply only options that want a boolean value, this may
     * change in the future
     * 
     * @param name      The name of the target requesting the toss
     * @param chance    The chance by which the toss-result should be determined as
     *                  success
     * @param operation The operation a toss is desired for, will be used to
     *                  distinguish injections
     * 
     * @return True if the request results in a success, false in case of a failure
     */
    public boolean requestFlip(String name, double chance, RandomOperation operation) {
        RandomControllerSettings nameSettings = settings.get(name);
        Queue<String> data;

        boolean flip;
        // if there is an injection for this name, it has data injected for the
        // operation and the data is present
        if (nameSettings != null && (data = nameSettings.get(operation)) != null && !data.isEmpty()) {
            // remove an entry, we do not care if it is not a sensible boolean value as this
            // might be
            // checked only if decoded by the Inject-Routine
            flip = data.remove().equalsIgnoreCase(TRUE);
            magpie.writeWarning("Injected random result for: " + name + AND_OP_TXT + operation + TO_TXT + flip
                    + ". Original chance: " + chance + ".", ENFORCE_TXT);
        } else {
            flip = RandomHelper.flip(chance);
        }

        author.storeRandomResult(name, operation, Boolean.toString(flip));
        return flip;
    }

    /**
     * This will request a random character by the Random-Controller which will
     * delegate the call to {@link RandomHelper#rndPick(List)} by default. This
     * behaviour is altered if and only if there is a injection matching the given
     * name and operation. In this case the controller will pop the given result and
     * return it so it may be used transparent from a real toss.
     * <p>
     * Any choice made, even if it was injected, will be written to the supplied
     * {@link StoryAuthor} (see {@link #RandomController(StoryAuthor)}).
     * <p>
     * It is up to you to supply only options that want a gadget enum, this may
     * change in the future
     * 
     * @param name      The name of the target requesting the toss
     * @param possibles The character to pick from; will be considered even for
     *                  loading by name
     * @param operation The operation a character is desired for, will be used to
     *                  distinguish injections
     * 
     * @return The character that was chosen
     */
    public Character requestCharacter(String name, Set<Character> possibles, RandomOperation operation) {
        RandomControllerSettings nameSettings = settings.get(name);
        Queue<String> data;

        Character target;
        // if there is an injection for this name, it has data injected for the
        // operation and the data is present
        if (nameSettings != null && (data = nameSettings.get(operation)) != null && !data.isEmpty()) {
            target = useInjectedCharacter(name, possibles, operation, data);
        } else {
            target = RandomHelper.rndPick(possibles);
        }

        author.storeRandomResult(name, operation, target.getName());
        return target;
    }

    private Character useInjectedCharacter(String name, Set<Character> possibles, RandomOperation operation,
            Queue<String> data) {
        Character target;
        // remove an entry, we do not care if it is not a sensible boolean value as this
        // might be
        // checked only if decoded by the Inject-Routine
        String guess = data.remove();

        Optional<Character> mayCharacter = possibles.stream().filter(c -> Objects.equals(c.getName(), guess)).findAny();

        if (!mayCharacter.isPresent()) {
            throw new StoryRandomArgumentException("The character you wanted for: " + name + " on operation: "
                    + operation + " which was: " + guess + " is not in the list of possibles: " + possibles);
        }
        target = mayCharacter.get();

        magpie.writeWarning("Injected random character for: " + name + AND_OP_TXT + operation + TO_TXT + target
                + ". Original possible list: " + possibles + ".", ENFORCE_TXT);
        return target;
    }

    /**
     * This will request a movement-choice by the Random-Controller which will
     * delegate the call to {@link RandomHelper#rndPick(List)} by default. This
     * behaviour is altered if and only if there is a injection matching the given
     * name and operation. In this case the controller will pop the given result and
     * return it so it may be used transparent from a real choose. The given current
     * position will be moved in case of an injection to determine the target point
     * <p>
     * Any choice made, even if it was injected, will be written to the supplied
     * {@link StoryAuthor} (see {@link #RandomController(StoryAuthor)}).
     * 
     * @param name      The name of the target requesting the toss
     * @param possibles Possible points to pick from
     * @param current   The current position to set relative coordinates from
     * @param operation The operation a toss is desired for, will be used to
     *                  distinguish injections
     * 
     * @return Target of the random movement operation
     */
    public Point requestPoint(String name, List<Point> possibles, Point current, RandomOperation operation) {
        return requestPoint(name, () -> RandomHelper.rndPick(possibles), current, operation);
    }

    /**
     * This will request a movement-choice by the Random-Controller which will
     * delegate the call to the supplied function by default. This behaviour is
     * altered if and only if there is a injection matching the given name and
     * operation. In this case the controller will pop the given result and return
     * it so it may be used transparent from a real choose. The given current
     * position will be moved in case of an injection to determine the target point
     * <p>
     * Any choice made, even if it was injected, will be written to the supplied
     * {@link StoryAuthor} (see {@link #RandomController(StoryAuthor)}).
     * 
     * @param name      The name of the target requesting the toss
     * @param producer  Function to be called to retrieve a point
     * @param current   The current position to set relative coordinates from
     * @param operation The operation a toss is desired for, will be used to
     *                  distinguish injections
     * 
     * @return Target of the random movement operation
     */
    public Point requestPoint(String name, Supplier<Point> producer, Point current, RandomOperation operation) {
        RandomControllerSettings nameSettings = settings.get(name);
        Queue<String> data;
        Point target;
        // if there is an injection for this name, it has data injected for the
        // operation and the data is present
        if (nameSettings != null && (data = nameSettings.get(operation)) != null && !data.isEmpty()) {
            target = useInjectedPoint(name, current, operation, data);
        } else {
            target = producer.get();
        }
        author.storeRandomResult(name, operation, target.toString());
        return target;
    }

    private Point useInjectedPoint(String name, Point current, RandomOperation operation, Queue<String> data) {
        // remove an entry, we do not care if it is not a sensible boolean value as this
        // might be
        // checked only if decoded by the Inject-Routine
        Point target;
        String enforcedData = data.remove();
        try {
            target = Point.fromString(enforcedData, current);
        } catch (PointParseException e) {
            throw new StoryRandomArgumentException("The Point data for character: " + name + " given as: "
                    + enforcedData + " is not valid. See the PointParseException: " + e.getLocalizedMessage());
        }
        magpie.writeWarning("Injected random movement for: " + name + AND_OP_TXT + operation + TO_TXT + enforcedData
                + ". Original position: " + current + ". Now at: " + target + ".", ENFORCE_TXT);
        return target;
    }

    /**
     * Inject a value into the random buffer
     * 
     * @param result    name:result1;result2;result3,....
     * @param operation The RandomOperation to target
     * 
     * @StoryMethod Used by the {@link StoryBoard} to inject values
     */
    @StoryMethod
    public void injectRandomResult(String result, String operation) {
        // value must be a valid operation
        RandomOperation randomOperation;
        try {
            randomOperation = RandomOperation.valueOf(operation.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new StoryArgumentException(generateStoryExceptionMessage(result, operation));
        }
        // check if the target signature is valid :D
        String[] data = result.split(":");
        if (data.length != 2) {
            throw new StoryArgumentException(
                    "The injection for: " + randomOperation + " failed as your specified results: " + result
                            + " do not match the pattern 'name:result1;result2;result3...'.");
        }

        // decode data
        final String name = data[0];
        final String[] results = data[1].split(";\\s*");
        // TODO: check types in the future?
        List<String> translatedResults = Arrays.stream(results).collect(Collectors.toList());

        injectRandomResult(randomOperation, name, translatedResults);
    }

    private String generateStoryExceptionMessage(String result, String operation) {
        return "You wanted to inject a random result by the op-name of: '" + operation + "' (" + operation.toUpperCase()
                + ") and target: " + result
                + " but the op-name does not represent a valid identifier. Choose one of (case insensitive):"
                + Arrays.toString(RandomOperation.values());
    }

    private void injectRandomResult(RandomOperation randomOperation, String name, List<String> translatedResults) {
        if (!this.settings.containsKey(name)) {
            this.settings.put(name, new RandomControllerSettings());
        }
        RandomControllerSettings settingsElement = this.settings.get(name);
        if (!settingsElement.containsKey(randomOperation)) {
            settingsElement.put(randomOperation, new LinkedList<>());
        }
        settingsElement.get(randomOperation).addAll(translatedResults);
    }

}