# 解释器模式

## 目的
给定一个语言，定义它的文法表示，并定义一个解释器，这个解释器使用该标识来解释语言中的句子，解释器模式提供了评估语言的语法或表达式的方式。

## 优点
1. 可扩展性比较好，灵活。
2. 增加了新的解释表达式的方式。
3. 易于实现简单文法。

## 缺点
1. 可利用场景比较少。
2. 对于复杂的文法比较难维护。
3. 解释器模式会引起类膨胀。
4. 解释器模式采用递归调用方法。

## 例子
解释器模式就是对一个表达式进行解析，直接看使用方式就能理解：
```java
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        String tokenString = "4 3 2 - 1 + *";
        Stack<Expression> stack = new Stack<>();

        String[] tokenList = tokenString.split(" ");
        for (String s : tokenList) {
            if (isOperator(s)) {
                Expression rightExpression = stack.pop();
                Expression leftExpression = stack.pop();
                LOGGER.info("popped from stack left: {} right: {}",
                        leftExpression.interpret(), rightExpression.interpret());
                Expression operator = getOperatorInstance(s, leftExpression, rightExpression);
                LOGGER.info("operator: {}", operator);
                int result = operator.interpret();
                NumberExpression resultExpression = new NumberExpression(result);
                stack.push(resultExpression);
                LOGGER.info("push result to stack: {}", resultExpression.interpret());
            } else {
                Expression i = new NumberExpression(s);
                stack.push(i);
                LOGGER.info("push to stack: {}", i.interpret());
            }
        }
        LOGGER.info("result: {}", stack.pop().interpret());
    }

    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*");
    }

    /**
     * Get expression for string
     */
    private static Expression getOperatorInstance(String s, Expression left, Expression right) {
        switch (s) {
            case "+":
                return new PlusExpression(left, right);
            case "-":
                return new MinusExpression(left, right);
            case "*":
                return new MultiplyExpression(left, right);
            default:
                return new MultiplyExpression(left, right);
        }
    }
}

/*
输出：
22:24:01.043 [main] INFO com.dhf.Application - push to stack: 4
22:24:01.055 [main] INFO com.dhf.Application - push to stack: 3
22:24:01.055 [main] INFO com.dhf.Application - push to stack: 2
22:24:01.055 [main] INFO com.dhf.Application - popped from stack left: 3 right: 2
22:24:01.055 [main] INFO com.dhf.Application - operator: -
22:24:01.055 [main] INFO com.dhf.Application - push result to stack: 1
22:24:01.055 [main] INFO com.dhf.Application - push to stack: 1
22:24:01.055 [main] INFO com.dhf.Application - popped from stack left: 1 right: 1
22:24:01.056 [main] INFO com.dhf.Application - operator: +
22:24:01.056 [main] INFO com.dhf.Application - push result to stack: 2
22:24:01.056 [main] INFO com.dhf.Application - popped from stack left: 4 right: 2
22:24:01.056 [main] INFO com.dhf.Application - operator: *
22:24:01.056 [main] INFO com.dhf.Application - push result to stack: 8
22:24:01.056 [main] INFO com.dhf.Application - result: 8
*/
```

另一个例子，这次使用另一个规则，比如`8 3 2 4 - + *`这样一个表达式，取出数字8 3和-运算符，计算得到5，于是表达式就变成了`5 2 4 + *`。然后，再取出5 2和+运算符，计算得到7，表达式就变成了`7 4 *`。最后，取出7 4和*运算符，最终得到的结果就是28。实现如下：
```java

public class ExpressionInterpreter {
    private Deque<Long> numbers = new LinkedList<>();

    public long interpret(String expression) {
        String[] elements = expression.split(" ");
        int length = elements.length;
        for (int i = 0; i < (length+1)/2; ++i) {
        numbers.addLast(Long.parseLong(elements[i]));
        }

        for (int i = (length+1)/2; i < length; ++i) {
        String operator = elements[i];
        boolean isValid = "+".equals(operator) || "-".equals(operator)
                || "*".equals(operator) || "/".equals(operator);
        if (!isValid) {
            throw new RuntimeException("Expression is invalid: " + expression);
        }

        long number1 = numbers.pollFirst();
        long number2 = numbers.pollFirst();
        long result = 0;
        if (operator.equals("+")) {
            result = number1 + number2;
        } else if (operator.equals("-")) {
            result = number1 - number2;
        } else if (operator.equals("*")) {
            result = number1 * number2;
        } else if (operator.equals("/")) {
            result = number1 / number2;
        }
        numbers.addFirst(result);
        }

        if (numbers.size() != 1) {
        throw new RuntimeException("Expression is invalid: " + expression);
        }

        return numbers.pop();
    }
}
```

上面的实现中，语法规则的解析逻辑都集中在一个函数中，对于简单的语法规则的解析，这样的设计就足够了，但是，对于复杂的语法规则的解析，逻辑复杂，代码量多，所有的解析逻辑都耦合在一个函数中，这样显然是不合适的。这个时候，就要考虑拆分代码，将解析逻辑拆分到独立的小类中。这个时候就可以用解释器模式，代码如下：
```java

public interface Expression {
    long interpret();
}

public class NumberExpression implements Expression {
    private long number;

    public NumberExpression(long number) {
        this.number = number;
    }

    public NumberExpression(String number) {
        this.number = Long.parseLong(number);
    }

    @Override
    public long interpret() {
        return this.number;
    }
}

public class AdditionExpression implements Expression {
    private Expression exp1;
    private Expression exp2;

    public AdditionExpression(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public long interpret() {
        return exp1.interpret() + exp2.interpret();
    }
}
// SubstractionExpression/MultiplicationExpression/DivisionExpression与AdditionExpression代码结构类似，这里就省略了

public class ExpressionInterpreter {
    private Deque<Expression> numbers = new LinkedList<>();

    public long interpret(String expression) {
        String[] elements = expression.split(" ");
        int length = elements.length;
        for (int i = 0; i < (length+1)/2; ++i) {
        numbers.addLast(new NumberExpression(elements[i]));
        }

        for (int i = (length+1)/2; i < length; ++i) {
        String operator = elements[i];
        boolean isValid = "+".equals(operator) || "-".equals(operator)
                || "*".equals(operator) || "/".equals(operator);
        if (!isValid) {
            throw new RuntimeException("Expression is invalid: " + expression);
        }

        Expression exp1 = numbers.pollFirst();
        Expression exp2 = numbers.pollFirst();
        Expression combinedExp = null;
        if (operator.equals("+")) {
            combinedExp = new AdditionExpression(exp1, exp2);
        } else if (operator.equals("-")) {
            combinedExp = new AdditionExpression(exp1, exp2);
        } else if (operator.equals("*")) {
            combinedExp = new AdditionExpression(exp1, exp2);
        } else if (operator.equals("/")) {
            combinedExp = new AdditionExpression(exp1, exp2);
        }
        long result = combinedExp.interpret();
        numbers.addFirst(new NumberExpression(result));
        }

        if (numbers.size() != 1) {
        throw new RuntimeException("Expression is invalid: " + expression);
        }

        return numbers.pop().interpret();
    }
}
```