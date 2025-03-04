package com.codeborne.selenide.collections;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ex.DoesNotContainTextsError;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import org.openqa.selenium.WebElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

@ParametersAreNonnullByDefault
public class ContainExactTextsCaseSensitive extends CollectionCondition {
  private final List<String> expectedTexts;

  public ContainExactTextsCaseSensitive(String... expectedTexts) {
    this(asList(expectedTexts));
  }

  public ContainExactTextsCaseSensitive(List<String> expectedTexts) {
    if (expectedTexts.isEmpty()) {
      throw new IllegalArgumentException("No expected texts given");
    }
    this.expectedTexts = unmodifiableList(expectedTexts);
  }

  @CheckReturnValue
  @Override
  public boolean test(List<WebElement> elements) {
    if (elements.size() < expectedTexts.size()) {
      return false;
    }

    return ElementsCollection
      .texts(elements)
      .containsAll(expectedTexts);
  }

  @Override
  public void fail(CollectionSource collection,
                   @Nullable List<WebElement> elements,
                   @Nullable Exception lastError,
                   long timeoutMs) {
    if (elements == null || elements.isEmpty()) {
      throw new ElementNotFound(collection, toString(), timeoutMs, lastError);
    }
    else {
      List<String> actualTexts = ElementsCollection.texts(elements);
      List<String> difference = new ArrayList<>(expectedTexts);
      difference.removeAll(actualTexts);
      throw new DoesNotContainTextsError(collection,
        expectedTexts, actualTexts, difference, explanation,
        timeoutMs, lastError);
    }
  }

  @Override
  public boolean missingElementSatisfiesCondition() {
    return false;
  }

  @Override
  public String toString() {
    return "Contains exact texts case-sensitive " + expectedTexts;
  }
}
