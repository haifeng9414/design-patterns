# 中介者模式

## 目的
在用户与用户直接聊天的设计方案中，用户对象之间存在很强的关联性，将导致系统出现如下问题：
1. 系统结构复杂：对象之间存在大量的相互关联和调用，若有一个对象发生变化，则需要跟踪和该对象关联的其他所有对象，并进行适当处理。
2. 对象可重用性差：由于一个对象和其他对象具有很强的关联，若没有其他对象的支持，一个对象很难被另一个系统或模块重用，这些对象表现出来更像一个不可分割的整体，职责较为混乱。
3. 系统扩展性低：增加一个新的对象需要在原有相关对象上增加引用，增加新的引用关系也需要调整原有对象，系统耦合度很高，对象操作很不灵活，扩展性差。

在面向对象的软件设计与开发过程中，根据“单一职责原则”，我们应该尽量将对象细化，使其只负责或呈现单一的职责。

对于一个模块，可能由很多对象构成，而且这些对象之间可能存在相互的引用，为了减少对象两两之间复杂的引用关系，使之成为一个松耦合的系统，我们需要使用中介者模式，这就是中介者模式的模式动机。

用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。中介者模式又称为调停者模式。

## 优点
1. 简化了对象之间的交互。
2. 将各同事解耦。
3. 减少子类生成。
4. 可以简化各同事类的设计和实现。

## 缺点
1. 在具体中介者类中包含了同事之间的交互细节，可能会导致具体中介者类非常复杂，使得系统难以维护。

## 例子
同事接口定义了同事的基本行为，同事抽象类持有中介者Party类，具体的同事对象执行动作时通过中介者Party类通知其他对象：
```java
// 同事接口
public interface PartyMember {
    void joinedParty(Party party);

    void partyAction(Action action);

    void act(Action action);
}

// 同事抽象类，持有中介者Party类，对同事之间的交互进行了代理
public abstract class PartyMemberBase implements PartyMember {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartyMemberBase.class);

    protected Party party;

    @Override
    public void joinedParty(Party party) {
        LOGGER.info("{} joins the party", this);
        this.party = party;
    }

    @Override
    public void partyAction(Action action) {
        LOGGER.info("{} {}", this, action.getDescription());
    }

    @Override
    public void act(Action action) {
        if (party != null) {
            LOGGER.info("{} {}", this, action);
            party.act(this, action);
        }
    }

    @Override
    public abstract String toString();
}

// Party接口
public interface Party {
    void addMember(PartyMember member);

    void act(PartyMember actor, Action action);
}

public class PartyImpl implements Party {
    private final List<PartyMember> members;

    public PartyImpl() {
        members = new ArrayList<>();
    }

    @Override
    public void act(PartyMember actor, Action action) {
        for (PartyMember member : members) {
            if (!member.equals(actor)) {
                member.partyAction(action);
            }
        }
    }

    @Override
    public void addMember(PartyMember member) {
        members.add(member);
        member.joinedParty(this);
    }
}

public class Application {
    /**
     * Program entry point
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        // create party and members
        Party party = new PartyImpl();
        Hobbit hobbit = new Hobbit();
        Wizard wizard = new Wizard();
        Rogue rogue = new Rogue();
        Hunter hunter = new Hunter();

        // add party members
        party.addMember(hobbit);
        party.addMember(wizard);
        party.addMember(rogue);
        party.addMember(hunter);

        // perform actions -> the other party members
        // are notified by the party
        hobbit.act(Action.ENEMY);
        wizard.act(Action.TALE);
        rogue.act(Action.GOLD);
        hunter.act(Action.HUNT);
    }
}

/*
输出：
23:33:11.433 [main] INFO com.dhf.mediator.PartyMemberBase - Hobbit joins the party
23:33:11.438 [main] INFO com.dhf.mediator.PartyMemberBase - Wizard joins the party
23:33:11.438 [main] INFO com.dhf.mediator.PartyMemberBase - Rogue joins the party
23:33:11.438 [main] INFO com.dhf.mediator.PartyMemberBase - Hunter joins the party
23:33:11.439 [main] INFO com.dhf.mediator.PartyMemberBase - Hobbit spotted enemies
23:33:11.439 [main] INFO com.dhf.mediator.PartyMemberBase - Wizard runs for cover
23:33:11.439 [main] INFO com.dhf.mediator.PartyMemberBase - Rogue runs for cover
23:33:11.439 [main] INFO com.dhf.mediator.PartyMemberBase - Hunter runs for cover
23:33:11.439 [main] INFO com.dhf.mediator.PartyMemberBase - Wizard tells a tale
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Hobbit comes to listen
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Rogue comes to listen
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Hunter comes to listen
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Rogue found gold
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Hobbit takes his share of the gold
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Wizard takes his share of the gold
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Hunter takes his share of the gold
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Hunter hunted a rabbit
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Hobbit arrives for dinner
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Wizard arrives for dinner
23:33:11.440 [main] INFO com.dhf.mediator.PartyMemberBase - Rogue arrives for dinner
*/
```