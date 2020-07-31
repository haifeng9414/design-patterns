# 中介者模式

## 目的
在用户与用户直接聊天的设计方案中，用户对象之间存在很强的关联性，将导致系统出现如下问题：
1. 系统结构复杂：对象之间存在大量的相互关联和调用，若有一个对象发生变化，则需要跟踪和该对象关联的其他所有对象，并进行适当处理。
2. 对象可重用性差：由于一个对象和其他对象具有很强的关联，若没有其他对象的支持，一个对象很难被另一个系统或模块重用，这些对象表现出来更像一个不可分割的整体，职责较为混乱。
3. 系统扩展性低：增加一个新的对象需要在原有相关对象上增加引用，增加新的引用关系也需要调整原有对象，系统耦合度很高，对象操作很不灵活，扩展性差。

在面向对象的软件设计与开发过程中，根据“单一职责原则”，我们应该尽量将对象细化，使其只负责或呈现单一的职责。

对于一个模块，可能由很多对象构成，而且这些对象之间可能存在相互的引用，为了减少对象两两之间复杂的引用关系，使之成为一个松耦合的系统，我们需要使用中介者模式，这就是中介者模式的模式动机。

用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。中介者模式又称为调停者模式。

举个例子，为了让飞机在飞行的时候互不干扰，每架飞机都需要知道其他飞机每时每刻的位置，这就需要时刻跟其他飞机通信。飞机通信形成的通信网络就会无比复杂。这个时候，通过引入塔台这样一个中介，让每架飞机只跟塔台来通信，发送自己的位置给塔台，由塔台来负责每架飞机的航线调度。这样就大大简化了通信网络。

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

另一个例子，假设有一个比较复杂的对话框，对话框中有很多控件，比如按钮、文本框、下拉框等。当对某个控件进行操作的时候，其他控件会做出相应的反应，比如，在下拉框中选择注册，注册相关的控件就会显示在对话框中。如果在下拉框中选择登录，登录相关的控件就会显示在对话框中。下面的简单的实现：
```java
public class UIControl {
    private static final String LOGIN_BTN_ID = "login_btn";
    private static final String REG_BTN_ID = "reg_btn";
    private static final String USERNAME_INPUT_ID = "username_input";
    private static final String PASSWORD_INPUT_ID = "pswd_input";
    private static final String REPEATED_PASSWORD_INPUT_ID = "repeated_pswd_input";
    private static final String HINT_TEXT_ID = "hint_text";
    private static final String SELECTION_ID = "selection";

    public static void main(String[] args) {
        Button loginButton = (Button)findViewById(LOGIN_BTN_ID);
        Button regButton = (Button)findViewById(REG_BTN_ID);
        Input usernameInput = (Input)findViewById(USERNAME_INPUT_ID);
        Input passwordInput = (Input)findViewById(PASSWORD_INPUT_ID);
        Input repeatedPswdInput = (Input)findViewById(REPEATED_PASSWORD_INPUT_ID);
        Text hintText = (Text)findViewById(HINT_TEXT_ID);
        Selection selection = (Selection)findViewById(SELECTION_ID);

        // 处理按钮点击逻辑
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.text();
                String password = passwordInput.text();
                //校验数据...
                //做业务处理...
            }
        });

        regButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            //获取usernameInput、passwordInput、repeatedPswdInput数据...
            //校验数据...
            //做业务处理...
            }
        });

        //...省略selection下拉选择框相关代码....
    }
}
```

上面的业务逻辑分别写在了各个组件的onClick方法中，下面再使用中介者模式实现：
```java

public interface Mediator {
    void handleEvent(Component component, String event);
}

public class LandingPageDialog implements Mediator {
    private Button loginButton;
    private Button regButton;
    private Selection selection;
    private Input usernameInput;
    private Input passwordInput;
    private Input repeatedPswdInput;
    private Text hintText;

    @Override
    public void handleEvent(Component component, String event) {
        if (component.equals(loginButton)) {
            String username = usernameInput.text();
            String password = passwordInput.text();
            //校验数据...
            //做业务处理...
        } else if (component.equals(regButton)) {
            //获取usernameInput、passwordInput、repeatedPswdInput数据...
            //校验数据...
            //做业务处理...
        } else if (component.equals(selection)) {
            String selectedItem = selection.select();
            if (selectedItem.equals("login")) {
                usernameInput.show();
                passwordInput.show();
                repeatedPswdInput.hide();
                hintText.hide();
                //...省略其他代码
            } else if (selectedItem.equals("register")) {
                //....
            }
        }
    }
}

public class UIControl {
    private static final String LOGIN_BTN_ID = "login_btn";
    private static final String REG_BTN_ID = "reg_btn";
    private static final String USERNAME_INPUT_ID = "username_input";
    private static final String PASSWORD_INPUT_ID = "pswd_input";
    private static final String REPEATED_PASSWORD_INPUT_ID = "repeated_pswd_input";
    private static final String HINT_TEXT_ID = "hint_text";
    private static final String SELECTION_ID = "selection";

    public static void main(String[] args) {
        Button loginButton = (Button)findViewById(LOGIN_BTN_ID);
        Button regButton = (Button)findViewById(REG_BTN_ID);
        Input usernameInput = (Input)findViewById(USERNAME_INPUT_ID);
        Input passwordInput = (Input)findViewById(PASSWORD_INPUT_ID);
        Input repeatedPswdInput = (Input)findViewById(REPEATED_PASSWORD_INPUT_ID);
        Text hintText = (Text)findViewById(HINT_TEXT_ID);
        Selection selection = (Selection)findViewById(SELECTION_ID);

        Mediator dialog = new LandingPageDialog();
        dialog.setLoginButton(loginButton);
        dialog.setRegButton(regButton);
        dialog.setUsernameInput(usernameInput);
        dialog.setPasswordInput(passwordInput);
        dialog.setRepeatedPswdInput(repeatedPswdInput);
        dialog.setHintText(hintText);
        dialog.setSelection(selection);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.handleEvent(loginButton, "click");
            }
        });

        regButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.handleEvent(regButton, "click");
            }
        });

        //....
    }
}
```

中介者模式将分散在各个控件中的原本业务逻辑都集中到了中介类中。实际上，这样做既有好处，也有坏处。好处是简化了控件之间的交互，坏处是中介类有可能会变成大而复杂的上帝类。所以，在使用中介模式的时候，要根据实际的情况，平衡对象之间交互的复杂度和中介类本身的复杂度。

