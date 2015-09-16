import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by joseph on 16/09/2015.
 */
public class GoogleGuavaExample {
    public static void main(String[] args) {
        GoogleGuavaExample example = new GoogleGuavaExample();
        System.out.println(example.join());
        // 10 Downing St, London, United Kingdom
    }

    public GoogleGuavaExample(){}

    public String join() {
        String street = Strings.emptyToNull("10 Downing St");
        String city = Strings.emptyToNull("London");
        String stateOrProvince = Strings.emptyToNull(StringUtils.EMPTY);
        String postalCode = Strings.emptyToNull(StringUtils.EMPTY);
        String country = Strings.emptyToNull("United Kingdom");

        Joiner joiner = Joiner.on(", ").skipNulls();

        return joiner.join(street, city, stateOrProvince, postalCode, country);
    }
}
