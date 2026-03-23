package com.switchboard.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.switchboard", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule domain_should_not_depend_on_application =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..")
            .because("domain layer must not depend on application layer");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_adapter =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..")
            .because("domain layer must not depend on adapter layer");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("domain layer must not depend on infrastructure layer");

    @ArchTest
    static final ArchRule domain_should_not_use_spring =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .because("domain layer must be framework-free");

    @ArchTest
    static final ArchRule domain_should_not_use_jakarta =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("jakarta..")
            .because("domain layer must be framework-free");

    @ArchTest
    static final ArchRule application_should_not_depend_on_adapter =
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..")
            .because("application layer must not depend on adapter layer");

    @ArchTest
    static final ArchRule application_should_not_depend_on_infrastructure =
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("application layer must not depend on infrastructure layer");

    @ArchTest
    static final ArchRule adapters_should_not_depend_on_each_other =
        noClasses()
            .that().resideInAPackage("..adapter.output.persistence..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.output.messaging..")
            .because("adapters must not depend on each other");

    @ArchTest
    static final ArchRule messaging_should_not_depend_on_persistence =
        noClasses()
            .that().resideInAPackage("..adapter.output.messaging..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.output.persistence..")
            .because("adapters must not depend on each other");

    @ArchTest
    static final ArchRule cache_should_not_depend_on_persistence =
        noClasses()
            .that().resideInAPackage("..adapter.output.cache..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.output.persistence..")
            .because("adapters must not depend on each other");

    @ArchTest
    static final ArchRule rest_should_not_depend_on_persistence =
        noClasses()
            .that().resideInAPackage("..adapter.input.rest..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.output.persistence..")
            .because("input adapters must not depend on output adapters");
}
