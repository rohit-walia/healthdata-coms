package org.hl7;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.segment.ISegment;
import org.hl7.segment.MSH;
import org.hl7.segment.ORC;
import org.hl7.segment.PID;
import org.hl7.segment.PV1;
import org.hl7.segment.RXD;
import org.hl7.segment.RXE;
import org.hl7.segment.RXO;
import org.hl7.segment.RXR;
import org.hl7.segment.TQ1;
import org.hl7.segment.ZPI;
import org.hl7.segment.ZQM;
import org.hl7.segment.ZRX;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class Hl7Message {
  private MSH msh;
  private PID pid;
  private PV1 pv1;
  private ORC orc;
  private RXO rxo;
  private RXE rxe;
  private List<TQ1> tq1;
  private RXR rxr;
  private RXD rxd;
  private ZPI zpi;
  private ZQM zqm;
  private ZRX zrx;

  /**
   * Returns the hl7 message as a String.
   */
  @SuppressWarnings("unchecked")
  public String printMessage() {
    List<ISegment> segments = new ArrayList<>();

    try {
      for (Field field : this.getClass().getDeclaredFields()) {
        if (!Modifier.isStatic(field.getModifiers())) {
          field.setAccessible(true);
          Object obj = field.get(this);

          if (obj instanceof List && ((List<Object>) obj).stream().allMatch(ISegment.class::isInstance)) {
            segments.addAll((List<ISegment>) obj);
          } else if (obj instanceof ISegment) {
            segments.add((ISegment) obj);
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Error while printing message", e);
    }

    StringBuilder sb = new StringBuilder();
    segments.stream()
        .filter(Objects::nonNull)
        .forEach(segment -> sb.append(segment.print()).append(System.lineSeparator()));

    return sb.delete(sb.lastIndexOf(System.lineSeparator()), sb.length()).toString();
  }

  /**
   * Convert a hl7 message from String to object.
   */
  public static Hl7Message fromString(String message) {
    String[] segments = message.split("\\r?\\n|\\r");
    Hl7MessageBuilder builder = Hl7Message.builder();

    for (String segment : segments) {
      String segmentId = segment.split("\\|")[0];

      switch (segmentId) {
        case MSH.segmentId -> builder.msh(MSH.fromString(segment));
        case PID.segmentId -> builder.pid(PID.fromString(segment));
        case PV1.segmentId -> builder.pv1(PV1.fromString(segment));
        case ORC.segmentId -> builder.orc(ORC.fromString(segment));
        case RXO.segmentId -> builder.rxo(RXO.fromString(segment));
        case RXE.segmentId -> builder.rxe(RXE.fromString(segment));
        case RXR.segmentId -> builder.rxr(RXR.fromString(segment));
        case RXD.segmentId -> builder.rxd(RXD.fromString(segment));
        case ZPI.segmentId -> builder.zpi(ZPI.fromString(segment));
        case ZQM.segmentId -> builder.zqm(ZQM.fromString(segment));
        case ZRX.segmentId -> builder.zrx(ZRX.fromString(segment));
        case TQ1.segmentId -> {
          if (builder.tq1 == null) {
            builder.tq1(new ArrayList<>());
          }
          builder.tq1.add(TQ1.fromString(segment));
        }
        default -> throw new IllegalArgumentException("Unsupported or Invalid message segment [" + segmentId + "]");
      }
    }

    return builder.build();
  }
}
