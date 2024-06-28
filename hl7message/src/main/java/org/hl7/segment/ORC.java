package org.hl7.segment;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.segment.datatype.ExtendedCompositePersonName;
import org.hl7.segment.datatype.TimingQuantity;

import java.util.Objects;

/**
 * ORC segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/ORC">Standard hl7 v2.5 spec - ORC segment</a>
 */
@Getter
@Setter
@Builder(toBuilder = true)
@Jacksonized
public class ORC implements ISegment {
  @Builder.Default
  private String orc_1_orderControl = "";
  @Builder.Default
  private String orc_3_fillerOrderNumber = "";
  @Builder.Default
  private String orc_4_placerGroupNumber = "";
  @Builder.Default
  private String orc_5_orderStatus = "";
  @Builder.Default
  private TimingQuantity orc_7_quantityTiming = TimingQuantity.builder().build();
  @Builder.Default
  private String orc_9_transactionDateTime = "";
  @Builder.Default
  private ExtendedCompositePersonName orc_12_orderingProvider = ExtendedCompositePersonName.builder().build();
  @Builder.Default
  private String orc_25_orderStatusModifier = "";

  public static final String segmentId = "ORC";

  @Override
  public String print() {
    return segmentId + "|" + orc_1_orderControl + "||" + orc_3_fillerOrderNumber + "|" + orc_4_placerGroupNumber
        + "|" + orc_5_orderStatus + "||" + orc_7_quantityTiming.print() + "||" + orc_9_transactionDateTime
        + "|||" + orc_12_orderingProvider.print() + "|||||||||||||" + orc_25_orderStatusModifier + "|||||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static ORC fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!Objects.equals(fields[0], segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    ORCBuilder orc = ORC.builder();
    getField(fields, 1).ifPresent(orc::orc_1_orderControl);
    getField(fields, 3).ifPresent(orc::orc_3_fillerOrderNumber);
    getField(fields, 4).ifPresent(orc::orc_4_placerGroupNumber);
    getField(fields, 5).ifPresent(orc::orc_5_orderStatus);
    getField(fields, 7).map(TimingQuantity::fromString).ifPresent(orc::orc_7_quantityTiming);
    getField(fields, 9).ifPresent(orc::orc_9_transactionDateTime);
    getField(fields, 12).map(ExtendedCompositePersonName::fromString).ifPresent(orc::orc_12_orderingProvider);
    getField(fields, 25).ifPresent(orc::orc_25_orderStatusModifier);
    return orc.build();
  }
}
