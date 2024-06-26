package org.hl7.segment;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.segment.component.CodedElement;

/**
 * RXO segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/RXO">Standard hl7 v2.5 spec - RXO segment</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class RXO implements ISegment {
  @Builder.Default
  private final CodedElement rxo_1_requestGiveCode = CodedElement.builder().build();
  @Builder.Default
  private final CodedElement rxo_20_indication = CodedElement.builder().build();

  public static final String segmentId = "RXO";

  @Override
  public String print() {
    return segmentId + "|" + rxo_1_requestGiveCode.print() + "|||||||||||||||||||" + rxo_20_indication.print() + "||||||||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static RXO fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!fields[0].equals(segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    RXOBuilder rxo = RXO.builder();
    getField(fields, 1).map(CodedElement::fromString).ifPresent(rxo::rxo_1_requestGiveCode);
    getField(fields, 20).map(CodedElement::fromString).ifPresent(rxo::rxo_20_indication);
    return rxo.build();
  }
}
