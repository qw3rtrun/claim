package com.github.trunov.claim;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Полный набор тестов проекта.
 *
 * Created by qw3rtrun on 12.10.14.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ CustomerTest.class, EmployeeTest.class, DepartmentTest.class, ClaimTest.class })
public class AllTests {
}
