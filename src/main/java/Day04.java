import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day04 {

    public static void main(String[] args) throws IOException, URISyntaxException {
//        var input = InputFileReader.readInputAsIterator("day04-puzzle.txt");
//
//        var passports = buildPassports(input);

        var input2 = InputFileReader.readInputAsStream("day04-puzzle.txt");

        var passports = buildPassports(input2);

        var validPassports = passports.stream().filter(Passport::valid).count();

        System.out.println("Valid passport count: " + validPassports);

        validPassports = passports.stream().filter(Passport::enhancedValid).count();

        System.out.println("Enhanced valid passport count: " + validPassports);
    }

    private static List<Passport> buildPassports(Stream<String> input) {

        StringBuilder passportsString = input.collect(
                StringBuilder::new,
                (StringBuilder s1, String s2) -> {
                    if (s2.trim().length() == 0) {
                        s1.append("\n");
                    } else {
                        s1.append(" ");
                        s1.append(s2);
                    }
                },
                StringBuilder::append
        );

        return passportsString.toString().lines().collect(
                () -> new ArrayList<Passport>(),
                (list, line) -> {
                    System.out.println(line);
                    var passportBuilder = new Passport.Builder();
                    var params = line.split(" ");
                    Arrays.stream(params).filter(Predicate.not(String::isEmpty)).forEach(
                            inputPair -> {
                                var inputs = inputPair.split(":");
                                try {
                                    Method buildMethod = passportBuilder.getClass().getMethod(inputs[0], String.class);
                                    buildMethod.invoke(passportBuilder, inputs[1]);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                    list.add(passportBuilder.build());
                },
                ArrayList::addAll
        );
    }

    private static List<Passport> buildPassports(Iterator<String> input) {

        var result = new ArrayList<Passport>();
        var passportBuilder = new Passport.Builder();

        while (input.hasNext()) {
            var line = input.next();
            if (line == null || line.trim().length() == 0) {
                result.add(passportBuilder.build());
            } else {
                var params = line.split(" ");

                Arrays.stream(params).forEach(
                        inputPair -> {
                            var inputs = inputPair.split(":");
                            try {
                                Method buildMethod = passportBuilder.getClass().getMethod(inputs[0], String.class);
                                buildMethod.invoke(passportBuilder, inputs[1]);
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        }
        result.add(passportBuilder.build());
        return result;
    }
}

class Passport {
    String byr;
    String iyr;
    String eyr;
    String hgt;
    String hcl;
    String ecl;
    String pid;
    String cid;

    @Override
    public String toString() {
        return "Passport{" +
                "byr='" + byr + '\'' +
                ", iyr='" + iyr + '\'' +
                ", eyr='" + eyr + '\'' +
                ", hgt='" + hgt + '\'' +
                ", hcl='" + hcl + '\'' +
                ", ecl='" + ecl + '\'' +
                ", pid='" + pid + '\'' +
                ", cid='" + cid + '\'' +
                '}';
    }

    public void setByr(String byr) {
        this.byr = byr;
    }

    public void setIyr(String iyr) {
        this.iyr = iyr;
    }

    public void setEyr(String eyr) {
        this.eyr = eyr;
    }

    public void setHgt(String hgt) {
        this.hgt = hgt;
    }

    public void setHcl(String hcl) {
        this.hcl = hcl;
    }

    public void setEcl(String ecl) {
        this.ecl = ecl;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public boolean valid() {
        return byr != null && iyr != null && eyr != null && hgt != null && hcl != null && ecl != null && pid != null;
    }

    public boolean enhancedValid() {
        return validYear(byr, 1920, 2002) && validYear(iyr, 2010, 2020) && validYear(eyr, 2020, 2030)
                && validHgt() && validHcl() && validEcl() && validPid();
    }

    private boolean validPid() {
        try {
            return pid != null && pid.length() == 9 && Integer.parseInt(pid) != Integer.MAX_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private boolean validEcl() {
        return ecl != null && Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(ecl);
    }

    private boolean validHcl() {
        try {
            return hcl != null && hcl.startsWith("#") && hcl.length() == 7 && Integer.parseInt(hcl.substring(1, 7), 16) != Integer.MAX_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validHgt() {
        try {
            return hgt != null && ((hgt.endsWith("cm") && Integer.parseInt(hgt.substring(0, hgt.indexOf("cm"))) >= 150 && Integer.parseInt(hgt.substring(0, hgt.indexOf("cm"))) <= 193)
                    || (hgt.endsWith("in") && Integer.parseInt(hgt.substring(0, hgt.indexOf("in"))) >= 59 && Integer.parseInt(hgt.substring(0, hgt.indexOf("in"))) <= 76));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validYear(String year, int min, int max) {
        try {
            return year != null && year.length() == 4 &&
                    Integer.parseInt(year) >= min && Integer.parseInt(year) <= max;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    static class Builder {
        String byr;
        String iyr;
        String eyr;
        String hgt;
        String hcl;
        String ecl;
        String pid;
        String cid;

        public Builder byr(String byr) {
            this.byr = byr;
            return this;
        }

        public Builder iyr(String iyr) {
            this.iyr = iyr;
            return this;
        }

        public Builder eyr(String eyr) {
            this.eyr = eyr;
            return this;
        }

        public Builder hgt(String hgt) {
            this.hgt = hgt;
            return this;
        }

        public Builder hcl(String hcl) {
            this.hcl = hcl;
            return this;
        }

        public Builder ecl(String ecl) {
            this.ecl = ecl;
            return this;
        }

        public Builder pid(String pid) {
            this.pid = pid;
            return this;
        }

        public Builder cid(String cid) {
            this.cid = cid;
            return this;
        }

        public Passport build() {
            Passport passport = new Passport();
            passport.setByr(this.byr);
            this.byr = null;
            passport.setCid(this.cid);
            this.cid = null;
            passport.setEcl(this.ecl);
            this.ecl = null;
            passport.setEyr(this.eyr);
            this.eyr = null;
            passport.setHcl(this.hcl);
            this.hcl = null;
            passport.setHgt(this.hgt);
            this.hgt = null;
            passport.setIyr(this.iyr);
            this.iyr = null;
            passport.setPid(this.pid);
            this.pid = null;
            return passport;
        }

    }
}