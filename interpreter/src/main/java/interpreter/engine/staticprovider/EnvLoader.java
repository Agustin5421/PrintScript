package interpreter.engine.staticprovider;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
  public static final Map<String, String> map =
      new HashMap<>(
          Map.ofEntries(
              new SimpleEntry<>("GRAVITY", "9.81"),
              new SimpleEntry<>("SPEED_OF_LIGHT", "299792458"),
              new SimpleEntry<>("PLANCK_CONSTANT", "6.62607015e-34"),
              new SimpleEntry<>("AVOGADRO_NUMBER", "6.02214076e23"),
              new SimpleEntry<>("BOLTZMANN_CONSTANT", "1.380649e-23"),
              new SimpleEntry<>("ELECTRON_MASS", "9.10938356e-31"),
              new SimpleEntry<>("PROTON_MASS", "1.6726219e-27"),
              new SimpleEntry<>("NEUTRON_MASS", "1.674927471e-27"),
              new SimpleEntry<>("ELEMENTARY_CHARGE", "1.602176634e-19"),
              new SimpleEntry<>("GAS_CONSTANT", "8.314462618"),
              new SimpleEntry<>("EARTH_RADIUS", "6371000"),
              new SimpleEntry<>("EARTH_MASS", "5.972e24"),
              new SimpleEntry<>("SOLAR_MASS", "1.989e30"),
              new SimpleEntry<>("LIGHT_YEAR", "9.4607e15"),
              new SimpleEntry<>("PARSEC", "3.0857e16"),
              new SimpleEntry<>("IS_CONSTANT", "true"),
              new SimpleEntry<>("UNIVERSAL_CONSTANT", "constant")));

  public static void addNewConstants(@NotNull String key, @NotNull String value) {
    map.put(key, value);
  }

  public static Map<String, String> getMap() {
    return map;
  }

  public static String getValue(String key) throws IllegalArgumentException {
    String variable = map.get(key);
    if (variable == null) {
      throw new IllegalArgumentException("Enviroment variable not found: " + key);
    }
    return variable;
  }
}
