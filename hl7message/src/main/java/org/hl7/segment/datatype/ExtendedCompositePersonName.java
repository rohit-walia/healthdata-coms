package org.hl7.segment.datatype;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.utils.Hl7MsgUtils;

import java.util.stream.Stream;

/**
 * This data type is called 'XCN (Extended Composite ID Number and Name for Persons)'.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/DataTypes/XCN">Standard hl7 v2.5 spec - Data type XCN</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class ExtendedCompositePersonName implements IComponent {
  @Builder.Default
  private String idNumber = "";
  @Builder.Default
  private String familyName = "";
  @Builder.Default
  private String givenName = "";

  @Override
  public String print() {
    return Hl7MsgUtils.getCompositeFieldString(idNumber, familyName, givenName);
  }

  @Override
  public boolean isEmpty() {
    return Stream.of(idNumber, familyName, givenName).allMatch(String::isBlank);
  }

  /**
   * Converts String to XCN (Extended Composite ID Number and Name for Persons) object.
   */
  public static ExtendedCompositePersonName fromString(String component) {
    String[] fields = component.split("\\".concat(CARET), -1);
    ExtendedCompositePersonNameBuilder extendedCompositePerson = ExtendedCompositePersonName.builder();
    getField(fields, 0).ifPresent(extendedCompositePerson::idNumber);
    getField(fields, 1).ifPresent(extendedCompositePerson::familyName);
    getField(fields, 2).ifPresent(extendedCompositePerson::givenName);
    return extendedCompositePerson.build();
  }
}
