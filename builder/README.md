# 建造者模式

## 目的
将一个复杂的构建与其表示相分离，使得同样的构建过程可以创建不同的表示。

## 优点
建造者独立出来，使得对象的创建过程易扩展。

创建出来的对象是只读。

如果参数之间存在依赖，或者某些参数是必填的，这些参数校验逻辑可以统一放到Builder的build方法中实现。

## 例子
如果类有很多属性，构造时可能会为不同属性赋值，这样构造函数就不能覆盖所有可能的构造行为，此时可以将构造过程和类本身分开，以满足不同的构造行为，如：
```java
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

public static void main(String[] args) {
    Person personWang = new Person.Builder()
            .name("小王")
            .age(25)
            .nationality(Nationality.CHINA)
            .skinColor(SkinColor.YELLOW)
            .build();

    Person personZhang = new Person.Builder()
            .name("小张")
            .age(28)
            .nationality(Nationality.USA)
            .skinColor(SkinColor.WHITE)
            .build();

    Person personLiu = new Person.Builder()
            .name("老王")
            .age(48)
            .nationality(Nationality.JAPAN)
            .skinColor(SkinColor.YELLOW)
            .build();

    LOGGER.info(personWang.toString());
    LOGGER.info(personZhang.toString());
    LOGGER.info(personLiu.toString());
}
```
