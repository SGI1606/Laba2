package com.company;

import java.util.*;
import java.lang.*;


/**
 * Class Calculator
 * @author George Sapozhkov

 */
class Calculator {
    private static String operators = "+-*/";
    private static String delimiters = "() " + operators;


    /** Метод проверяющий является ли элемент разделителем
     * @param token запаренный элемент, число или знак.
     * @return true- если разделитель false-нет
     */
    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }


    /** Метод проверяющий является ли элемент оператором
     * @param token запаренный элемент, число или знак.
     * @return true- если оператор false-нет
     */
    private static boolean isOperator(String token) {
        if (token.equals("u-")) return true;
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }


    /** Метод проверяющий является ли элемент функцией
     * @param token запаренный элемент, число или знак.
     * @return true- если оператор false-нет
     */
    private static boolean isFunction(String token) {
        if (token.equals("sqrt") || token.equals("cube") || token.equals("pow5")) return true;
        return false;
    }


    /** Метод расставляющий приоритеты для операторов
     * @param token оператор
     * @return приоритет
     */
    private static int priority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("*") || token.equals("/")) return 3;
        return 4;
    }


    /** Метод преобразовывающий выражение в вид польской обратной аннотации
     * @param infix строка с выражением
     * @return Список элементов в котором выражение в ПОА
     * @throws Exception если не можем найти пару для скобки или есть знак, но нет продолжения
     **/
    public static List<String> parse(String infix) throws Exception {
        List<String> postfix = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                throw new Exception("Некорректное выражение.");
            }
            if (curr.equals(" ")) continue;
            if (isFunction(curr)) stack.push(curr);
            else if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    if (stack.isEmpty()) {
                        throw new Exception("Скобки не согласованы");
                    }
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            throw new Exception("Скобки не согласованы");
                        }
                    }

                    stack.pop();
                    if (!stack.isEmpty() && isFunction(stack.peek())) {
                        postfix.add(stack.pop());
                    }
                }
                else {
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev)  && !prev.equals(")")))) {
                        // унарный минус
                        curr = "u-";
                    }
                    else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }

                    }
                    stack.push(curr);
                }

            }
            else {
                postfix.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) postfix.add(stack.pop());
            else {
                throw new Exception("Скобки не согласованы");
            }
        }
        return postfix;
    }


    /** Метод считывающий лексемы и делающий соответствующие операции
     * @param postfix запись в форме ПОА
     * @return Финальный ответ калькулятора
     **/
    public static Double calc(List<String> postfix) {
        Deque<Double> stack = new ArrayDeque<Double>();
        for (String x : postfix) {
            if (x.equals("sqrt")) stack.push(Math.sqrt(stack.pop()));
            else if (x.equals("cube")) {
                Double tmp = stack.pop();
                stack.push(tmp * tmp * tmp);
            }
            else if (x.equals("pow5")) stack.push(Math.pow(stack.pop(),5));
            else if (x.equals("+")) stack.push(stack.pop() + stack.pop());
            else if (x.equals("-")) {
                Double b = stack.pop(), a = stack.pop();
                stack.push(a - b);
            }
            else if (x.equals("*")) stack.push(stack.pop() * stack.pop());
            else if (x.equals("/")) {
                Double b = stack.pop(), a = stack.pop();
                stack.push(a / b);
            }
            else if (x.equals("u-")) stack.push(-stack.pop());
            else stack.push(Double.valueOf(x));
        }
        return stack.pop();
    }
}
