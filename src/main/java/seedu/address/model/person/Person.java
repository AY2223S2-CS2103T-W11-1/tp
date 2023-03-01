package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import seedu.address.model.item.Identifiable;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person implements Identifiable {

    private final String id;

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Remark remark;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Creates a Person with the given id, name, phone, email and address.
     *
     * @param id      the id of the person.
     * @param name    the name of the person.
     * @param phone   the phone number of the person.
     * @param email   the email of the person.
     * @param address the address of the person.
     * @param tags    the tags of the person.
     * @param remark  the remarks of the person
     */
    public Person(String id, Name name, Phone phone, Email email,
                  Address address, Set<Tag> tags, Remark remark) {
        requireAllNonNull(id, name, phone, email, address, tags, remark);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.remark = remark;
    }

    /**
     * Creates a Person with the given id, name, phone, email and address.
     *
     * @param id      the id of the person.
     * @param name    the name of the person.
     * @param phone   the phone number of the person.
     * @param email   the email of the person.
     * @param address the address of the person.
     * @param tags    the tags of the person.
     */
    public Person(String id, Name name, Phone phone, Email email,
                  Address address, Set<Tag> tags) {
        requireAllNonNull(id, name, phone, email, address, tags);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.remark = null;
    }

    /**
     * Creates a Person with the given name, phone, email and address and a
     * random {@code UUID} as the id.
     *
     * @param name    the name of the person.
     * @param phone   the phone number of the person.
     * @param email   the email of the person.
     * @param address the address of the person.
     * @param tags    the tags of the person.
     * @see Person#Person(String, Name, Phone, Email, Address, Set)
     */
    public Person(Name name, Phone phone, Email email,
                  Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.remark = null;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail())
                .append("; Address: ")
                .append(getAddress());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
