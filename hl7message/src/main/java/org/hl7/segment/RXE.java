package org.hl7.segment;

import static org.hl7.segment.datatype.IComponent.CARET;
import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.RandomStringUtils;
import org.hl7.segment.datatype.CodedElement;

/**
 * RXE segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/RXE">Standard hl7 v2.5 spec - RXE segment</a>
 */
@Getter
@Setter
@Builder(toBuilder = true)
@Jacksonized
public class RXE implements ISegment {
  @Builder.Default
  private String rxe_2_1_drugNdc = RandomStringUtils.randomNumeric(11);
  @Builder.Default
  private String rxe_2_2_drugName = "Default RXE.2.2 medication name. Msg fails without this.";
  @Builder.Default
  private String rxe_3_giveAmountMinimum = EMPTY;
  @Builder.Default
  private String rxe_5_giveUnits = EMPTY;
  @Builder.Default
  private String rxe_6_giveDosageForm = EMPTY;
  @Builder.Default
  private String rxe_7_adminInstructionsText = "Default RXE.7 instructions. Msg fails without this.";
  @Builder.Default
  private String rxe_10_dispenseAmount = EMPTY;
  @Builder.Default
  private String rxe_15_prescriptionNumber = RandomStringUtils.randomNumeric(5);
  @Builder.Default
  private CodedElement rxe_27_giveIndication = CodedElement.builder().build();
  @Builder.Default
  private String rxe_25_giveStrength = EMPTY;
  @Builder.Default
  private String rxe_26_giveStrengthUnit = EMPTY;

  @Builder.Default
  private final String rxe_35_controlledSubstanceSchedule = EMPTY;

  public static final String segmentId = "RXE";

  @Override
  public String print() {
    return segmentId + "||" + rxe_2_1_drugNdc + "^" + rxe_2_2_drugName + "^||||" + rxe_6_giveDosageForm + "|^"
        + rxe_7_adminInstructionsText + "|||" + rxe_10_dispenseAmount + "|||||" + rxe_15_prescriptionNumber + "||||||||||"
        + rxe_25_giveStrength + "|" + rxe_26_giveStrengthUnit + "|"
        + rxe_27_giveIndication.print() + "||||||||" + rxe_35_controlledSubstanceSchedule + "|||||||||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static RXE fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!fields[0].equals(segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    RXEBuilder rxe = RXE.builder();
    getField(fields, 2).ifPresent(field -> {
      String[] subFields = field.split("\\".concat(CARET));
      rxe.rxe_2_1_drugNdc(subFields[0])
          .rxe_2_2_drugName(subFields[1]);
    });
    getField(fields, 3).ifPresent(rxe::rxe_3_giveAmountMinimum);
    getField(fields, 5).ifPresent(rxe::rxe_5_giveUnits);
    getField(fields, 6).ifPresent(rxe::rxe_6_giveDosageForm);
    getField(fields, 7).map(field -> field.split("\\".concat(CARET))[1]).ifPresent(rxe::rxe_7_adminInstructionsText);
    getField(fields, 10).ifPresent(rxe::rxe_10_dispenseAmount);
    getField(fields, 15).ifPresent(rxe::rxe_15_prescriptionNumber);
    getField(fields, 25).ifPresent(rxe::rxe_25_giveStrength);
    getField(fields, 26).ifPresent(rxe::rxe_26_giveStrengthUnit);
    getField(fields, 27).map(CodedElement::fromString).ifPresent(rxe::rxe_27_giveIndication);
    getField(fields, 35).ifPresent(rxe::rxe_35_controlledSubstanceSchedule);
    return rxe.build();
  }
}
