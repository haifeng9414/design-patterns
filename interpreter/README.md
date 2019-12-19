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