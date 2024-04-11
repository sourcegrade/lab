/**
 * A number range defines a range of numbers. The range is inclusive.
 */
interface NumberRange { min: number; max: number }
/**
 * A point range defines a range of points. The range is inclusive.
 */
type PointRange = number | NumberRange;

/**
 * A showcase string is a string that can either be a simple string or an object that contains a text and an html property. The text property is used for the text representation of the string and the html property is used for the html representation of the string. Both representations should contain the same information.
 */
type ShowcaseString = string | { text: string; html: HTMLElement };

/**
 * The state of a test
 */
enum TestState {
    /**
     * The test is pending, i.e. it has not been executed yet
     */
    PENDING = "PENDING",
    /**
     * The test is running, i.e. it is currently being executed
     */
    RUNNING = "RUNNING",
    /**
     * The test is successful, i.e. it has been executed and the result is positive
     */
    SUCCESS = "SUCCESS",
    /**
     * The test is not successful, i.e. it has been executed and the result is negative but no exception has been thrown
     */
    FAILURE = "FAILURE",
    /**
     * The test is not successful, i.e. it has been executed and the result is negative and an exception has been thrown
     */
    ERROR = "ERROR",
    /**
     * The test has been skipped, i.e. it has not been executed. This is the case if a test is skipped by the user or if a test is skipped because a prerequisite test has failed.
     */
    SKIPPED = "SKIPPED",
    /**
     * The state of the test is unknown, this is most likely due to an error in the test runner
     */
    UNKNOWN = "UNKNOWN",
}

/**
 * A test run is the result of a test. It contains information about the test such as the state, the message, the stacktrace, the duration and the children of the test.
 */
interface TestRun {
    /**
     * The id of the test
     */
    id: string;
    /**
     * The name of the test
     */
    name: string;
    /**
     * The state of the test
     */
    state: TestState;
    /**
     * The message of the test. It should be empty if the test is successful. If the test is not successful, it should contain the error message as well as context information.
     */
    message?: ShowcaseString;
    /**
     * The stacktrace of the test
     */
    stacktrace?: string;
    /**
     * The duration of the test in milliseconds
     */
    duration: number;
    /**
     * The children of the test
     */
    children?: TestRun[];
}

/**
 * The accumulator that is used to determine the achieved points of a criterion
 */
enum CriterionAccumulator {
    /**
     * This is the default accumulator. It is used if no other accumulator is specified. It requires all tests to be successful and sums up to possiblePoints.max. If any test is not successful, the sum is possiblePoints.min.
     */
    AND = "AND",
    /**
     * This accumulator requires at least one test to be successful and sums up to possiblePoints.max. If no test is successful, the sum is possiblePoints.min.
     */
    OR = "OR",
    /**
     * This accumulator requires no test to be successful and sums up to possiblePoints.max. If any test is successful, the sum is possiblePoints.min.
     */
    NOT = "NOT",
    /**
     * This accumulator starts with possiblePoints.min and adds 1 point for each successful test. The result is clamped to the range [possiblePoints.min, possiblePoints.max].
     */
    SUM = "SUM",
};

/**
 * A criterion is a part of a rubric.
 */
interface Criterion {
    /**
     * The id of the criterion
     */
    id: string;
    /**
     * The name of the criterion
     */
    name: ShowcaseString;
    /**
     * The description of the criterion
     */
    description: ShowcaseString;
    /**
     * The possible amount of points that can be achieved
     */
    possiblePoints: NumberRange;
    /**
     * The points that have been achieved. Can be a range if the points are not fixed yet
     */
    achievedPoints: PointRange;
    /**
     * The message that is displayed if the criterion is not fulfilled
     */
    message?: ShowcaseString;
    /**
     * Tests that were used to determine the achieved points
     */
    tests?: TestRun[];
    /**
     * The accumulator that is used to determine the achieved points of the criterion.
     */
    testAccumulator?: CriterionAccumulator;
    /**
     * The children of the criterion
     */
    children?: Criterion[];
    /**
     * The accumulator that is used to determine the achieved points of the children of the criterion.
     */
    childrenAccumulator?: CriterionAccumulator;
}

/**
 * A leaf criterion is a criterion that does not have any children.
 */
type LeafCriterion = Omit<Criterion, "children" | "childrenAccumulator"> & {
    tests: TestRun[];
    testAccumulator: CriterionAccumulator;
};

/**
 *  A parent criterion is a criterion that has children.
 */
type ParentCriterion = Omit<Criterion, "tests" | "testAccumulator"> & {
    children: Criterion[];
    childrenAccumulator: CriterionAccumulator;
};

/**
 * A rubric is a collection of criteria. It is used to provide feedback to the student about their solution.
 */
interface Rubric {
    /**
     * The id of the rubric
     */
    id: string;
    /**
     * The name of the rubric. This should contain information about the exercise such as sheet-number, group, etc.
     */
    name: ShowcaseString;
    /**
     * The description of the rubric
     */
    description: ShowcaseString;
    /**
     * The possible amount of points that can be achieved
     */
    possiblePoints: NumberRange;
    /**
     * The points that have been achieved. Can be a range if the points are not fixed yet
     */
    achievedPoints: PointRange;
    /**
     * The criteria of the rubric
     */
    criteria: Criterion[];
}