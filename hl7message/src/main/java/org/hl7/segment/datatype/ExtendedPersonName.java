package org.hl7.segment.datatype;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.utils.Hl7MsgUtils;

import java.util.stream.Stream;

/**
 * This data type is called 'XPN (Extended Person Name)'.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/DataTypes/XPN">Standard hl7 v2.5 spec - Data type XPN</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class ExtendedPersonName implements IComponent {
  @Builder.Default
  private String familyName = "";
  @Builder.Default
  private String givenName = "";

  @Override
  public String print() {
    return Hl7MsgUtils.getCompositeFieldString(familyName, givenName);
  }

  @Override
  public boolean isEmpty() {
    return Stream.of(familyName, givenName).allMatch(String::isBlank);
  }

  /**
   * Converts String to XCN (Extended Composite ID Number and Name for Persons) object.
   */
  public static ExtendedPersonName fromString(String component) {
    String[] fields = component.split("\\".concat(CARET), -1);
    ExtendedPersonName.ExtendedPersonNameBuilder extendedPersonName = ExtendedPersonName.builder();
    getField(fields, 0).ifPresent(extendedPersonName::familyName);
    getField(fields, 1).ifPresent(extendedPersonName::givenName);
    return extendedPersonName.build();
  }
}
