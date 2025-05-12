package dev.strafbefehl.deluxehubreloaded.config;

/**
 * Represents a version in the format major.minor.patch
 * Used for tracking plugin versions and migrations
 */
public class Version implements Comparable<Version> {
    private final int major;
    private final int minor;
    private final int patch;

    /**
     * Constructs a version from major, minor, and patch values
     */
    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Parses a version string in the format "major.minor.patch"
     */
    public static Version parse(String versionStr) {
        String[] parts = versionStr.split("\\.");

        int major = 0, minor = 0, patch = 0;

        if (parts.length >= 1) {
            try {
                major = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }

        if (parts.length >= 2) {
            try {
                minor = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }

        if (parts.length >= 3) {
            try {
                patch = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }

        return new Version(major, minor, patch);
    }

    /**
     * Compares this version with another version
     */
    @Override
    public int compareTo(Version other) {
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }

        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }

        return Integer.compare(this.patch, other.patch);
    }

    /**
     * Checks if this version is newer than another version
     */
    public boolean isNewerThan(Version other) {
        return compareTo(other) > 0;
    }

    /**
     * Checks if this version is the same as another version
     */
    public boolean isSameAs(Version other) {
        return compareTo(other) == 0;
    }

    /**
     * Checks if this version is older than another version
     */
    public boolean isOlderThan(Version other) {
        return compareTo(other) < 0;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}