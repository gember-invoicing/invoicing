package nl.marcenschede.invoice.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "~@not_implemented",
        format = {"pretty", "html:target/cucumber"},
        glue = {"nl.marcenschede.invoice.features.glue"},
        features = "classpath:features/",
        strict = true
)
public class AllCucumberTest {
}
