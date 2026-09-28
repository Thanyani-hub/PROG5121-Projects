PROG5121 Programming POE_Part1
Mavhungu Thanyani Bright
ST10190374

/*
 * Cell phone regex: "+27" followed by exactly nine digits.
 * Regex syntax (anchors ^ and $, \d, quantifier {n}, escaping) based on:
 * Oracle (n.d.) Class Pattern. Java Platform, Standard Edition 8 API
 * Specification. Available at:
 * https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
 * (Accessed: 28 September 2026).
 */
private static final Pattern CELL_PATTERN = Pattern.compile("^\\+27\\d{9}$");


References
Oracle (n.d.) Class Pattern. Java Platform, Standard Edition 8 API Specification. Available at: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html (Accessed: 28 September 2026).
