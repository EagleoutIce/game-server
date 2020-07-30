/**
 * The main goal for this package is to achieve integration-level-tests on an
 * unit-test level. This allows to just include the server into the test-scope
 * of another package. For the major classes, see
 * {@link de.uulm.team020.server.core.dummies.DummyClient DummyClient},
 * {@link de.uulm.team020.server.core.dummies.DummyNttsController
 * DummyNttsController},
 * {@link de.uulm.team020.server.core.dummies.DummySendMessagesBuffer
 * DummySendMessagesBuffer}, and the story-sub-package.
 * <p>
 * If you want to test your own client logic you may use
 * {@link de.uulm.team020.server.core.dummies.story.AbstractStoryProtagonist} or
 * the {@link de.uulm.team020.server.core.dummies.story.AbstractStoryHero}.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/26/2020
 */
package de.uulm.team020.server.core.dummies;