package events.application.port.in.request;

import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.request.LockerRange;
import com.jnulocker.events.application.port.in.request.PrefixInfo;
import java.util.ArrayList;
import java.util.List;

public class FloorInfoTestDataBuilder {
    private Integer floorNumber = 1;
    private List<PrefixInfo> prefixes = new ArrayList<>();

    private FloorInfoTestDataBuilder() {
        // 기본적으로 하나의 PrefixInfo와 LockerRange 추가
        prefixes.add(new PrefixInfo("A", List.of(new LockerRange(1, 20))));
    }

    public static FloorInfoTestDataBuilder floorInfoBuilder() {
        return new FloorInfoTestDataBuilder();
    }

    public FloorInfoTestDataBuilder withFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
        return this;
    }

    public FloorInfoTestDataBuilder withPrefix(String lockerPrefix, List<LockerRange> ranges) {
        this.prefixes.add(new PrefixInfo(lockerPrefix, ranges));
        return this;
    }

    public FloorInfoTestDataBuilder withPrefix(
            String lockerPrefix, Integer startNumber, Integer endNumber) {
        this.prefixes.add(
                new PrefixInfo(lockerPrefix, List.of(new LockerRange(startNumber, endNumber))));
        return this;
    }

    public FloorInfoTestDataBuilder withPrefixes(List<PrefixInfo> prefixes) {
        this.prefixes = new ArrayList<>(prefixes);
        return this;
    }

    public FloorInfo build() {
        return new FloorInfo(floorNumber, prefixes);
    }
}
