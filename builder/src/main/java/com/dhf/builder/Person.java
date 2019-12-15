package com.dhf.builder;

/**
 * 人
 */
public final class Person {
    private final String name;
    private final Integer age;
    private final Nationality nationality;
    private final SkinColor skinColor;

    public Person(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.skinColor = builder.skinColor;
        this.nationality = builder.nationality;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public SkinColor getSkinColor() {
        return skinColor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (null != nationality) {
            sb.append(" 是来自 ").append(nationality.toString()).append(" 的");
        }
        if (null != age) {
            sb.append(" ").append(age).append("岁的");
        }
        if (null != skinColor) {
            sb.append(" 有着").append(skinColor).append("皮肤的");
        }
        sb.append("一个人");
        return sb.toString();
    }

    /**
     * 创建者
     */
    public static class Builder {

        private String name;
        private Integer age;
        private Nationality nationality;
        private SkinColor skinColor;

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder name(String name) {
            if (null == name) {
                throw new IllegalArgumentException("人必须有名字!");
            }
            this.name = name;
            return this;
        }

        public Builder nationality(Nationality nationality) {
            this.nationality = nationality;
            return this;
        }

        public Builder skinColor(SkinColor skinColor) {
            this.skinColor = skinColor;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }
}
