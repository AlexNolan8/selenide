package com.codeborne.selenide.collections;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.MatcherError;
import com.codeborne.selenide.impl.CollectionSource;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;

import static com.codeborne.selenide.ElementsCollection.elementsToString;

@ParametersAreNonnullByDefault
public abstract class PredicateCollectionCondition extends CollectionCondition {
  protected final String matcher;
  protected final String description;
  protected final Predicate<WebElement> predicate;

  protected PredicateCollectionCondition(String matcher, String description, Predicate<WebElement> predicate) {
    this.matcher = matcher;
    this.description = description;
    this.predicate = predicate;
  }

  @Override
  public void fail(CollectionSource collection,
                   @Nullable List<WebElement> elements,
                   @Nullable Exception lastError,
                   long timeoutMs) {
    if (elements == null || elements.isEmpty()) {
      throw new ElementNotFound(collection, toString(), timeoutMs, lastError);
    } else {
      String expected = String.format("%s of elements to match [%s] predicate", matcher, description);
      throw new MatcherError(explanation,
        expected,
        elementsToString(collection.driver(), elements),
        collection, lastError, timeoutMs);
    }
  }

  @Override
  public boolean missingElementSatisfiesCondition() {
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s match [%s] predicate", matcher, description);
  }
}
