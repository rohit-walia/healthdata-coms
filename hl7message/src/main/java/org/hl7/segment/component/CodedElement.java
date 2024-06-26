package org.hl7.segment.component;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.utils.Hl7MsgUtils;

import java.util.stream.Stream;

/**
 * This data type, called 'Coded Element' transmits codes and the text associated with the code.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/DataTypes/CE">Standard hl7 v2.5 spec - Data type CE</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class CodedElement implements IComponent {
  @Builder.Default
  private final String id = "";
  @Builder.Default
  private final String text = "";
  @Builder.Default
  private final String system = "";

  @Override
  public String print() {
    return Hl7MsgUtils.getCompositeFieldString(id, text, system);
  }

  @Override
  public boolean isEmpty() {
    return Stream.of(id, text, system).allMatch(String::isBlank);
  }

  /**
   * Converts String to CodedElement object.
   */
  public static CodedElement fromString(String component) {
    String[] fields = component.split("\\".concat(CARET), -1);
    CodedElementBuilder codedElement = CodedElement.builder();
    getField(fields, 0).ifPresent(codedElement::id);
    getField(fields, 1).ifPresent(codedElement::text);
    getField(fields, 2).ifPresent(codedElement::system);
    return codedElement.build();
  }
}
