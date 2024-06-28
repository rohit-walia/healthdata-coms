package org.hl7.segment;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.segment.datatype.CodedElement;

/**
 * RXR segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/RXR">Standard hl7 v2.5 spec - RXR segment</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class RXR implements ISegment {
  public static final String segmentId = "RXR";
  @Builder.Default
  private CodedElement rxr_1_route = CodedElement.builder().build();

  @Override
  public String print() {
    return segmentId + "|" + rxr_1_route.print() + "|||||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static RXR fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!fields[0].equals(segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    RXRBuilder rxr = RXR.builder();
    getField(fields, 1).map(CodedElement::fromString).ifPresent(rxr::rxr_1_route);
    return rxr.build();
  }
}
