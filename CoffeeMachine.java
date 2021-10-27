package machine;

import java.util.Scanner;

class Machine {

    // Глобальное состояние кофе-машины
    private enum State {
        menu, buy, fill, take, remaining, exit
    }

    // Состояние при покупке
    private enum BuyState {
        nothing, espresso, latte, cappuccino, back
    }

    // Состояние при заполнении
    private enum FillState {
        load, water, milk, beans, cups
    }

    private State state;
    private BuyState buyState;
    private FillState fillState;

    //входные параметры для кофе-машины
    private int amountMoney;
    private int amountWater;
    private int amountMilk;
    private int amountBeans;
    private int amountCups;

    //инициализаця состояния кофе-машины
    private void initState() {
        this.state = State.menu;
        this.buyState = BuyState.nothing;
        this.fillState = FillState.load;
    }

    // Конструктор
    public Machine() {
        this.amountMoney = 550;
        this.amountWater = 400;
        this.amountMilk  = 540;
        this.amountBeans = 120;
        this.amountCups  = 9;
        initState();
        doAction(null);
    }

    //Печать состояния кофе-машины
    public void printState() {
        System.out.println("The coffee machine has:");
        System.out.printf("%s ml of water\n", this.amountWater);
        System.out.printf("%s ml of milk\n", this.amountMilk);
        System.out.printf("%s g of coffee beans\n", this.amountBeans);
        System.out.printf("%s disposable cups\n", this.amountCups);
        System.out.printf("$%s of money\n", this.amountMoney);
    }

    //Хватит ингредиентов?
    private boolean checkIngredients(int pWater, int pMilk, int pBeans) {
        boolean bResult = true;
        if (pWater > amountWater){
            System.out.println("Sorry, not enough water!");
            bResult = false;
        } else if (pMilk > amountMilk){
            System.out.println("Sorry, not enough milk!");
            bResult = false;
        } else if (pBeans > amountBeans) {
            System.out.println("Sorry, not enough coffee beans!");
            bResult = false;
        } else if (amountCups == 0) {
            System.out.println("Sorry, not enough disposable cups!");
            bResult = false;
        }
        return bResult;
    }

    //Действие = покупка
    private void doBuy() {
        switch (buyState) {
            case nothing:
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                break;
            case espresso:
                if (checkIngredients(250,0,16)) {
                    System.out.println("I have enough resources, making you a coffee!");
                    amountMoney += 4;
                    amountWater -= 250;
                    amountBeans -= 16;
                    amountCups -= 1;
                }
                break;
            case latte:
                if (checkIngredients(350,75,20)) {
                    System.out.println("I have enough resources, making you a coffee!");
                    amountMoney += 7;
                    amountWater -= 350;
                    amountMilk -= 75;
                    amountBeans -= 20;
                    amountCups -= 1;
                }
                break;
            case cappuccino:
                if (checkIngredients(200,100,12)) {
                    System.out.println("I have enough resources, making you a coffee!");
                    amountMoney += 6;
                    amountWater -= 200;
                    amountMilk -= 100;
                    amountBeans -= 12;
                    amountCups -= 1;
                }
                break;
            case back:
                break;
        }
        if (buyState != BuyState.nothing) {
            initState();
            doAction(null);
        }
    }

    // В строке только числа?
    static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    //Действие = наполнение
    private boolean doFill(String action) {
        boolean bResult = true;
        if (fillState == FillState.load) {
            System.out.println("Write how many ml of water do you want to add:");
            fillState = FillState.water;
        } else {
            int amount = 0;
            boolean bError = false;
            if (isNumeric(action)) {
                try {
                    amount = Integer.parseInt(action);
                } catch (NumberFormatException e) {
                    bError = true;
                }
            } else {
                bError = true;
            }
            if (bError) {
                bResult = false;
                System.out.println("Invalid input");
            } else {
                switch (fillState) {
                    case water:
                        amountWater += amount;
                        fillState = FillState.milk;
                        System.out.println("Write how many ml of milk do you want to add:");
                        break;
                    case milk:
                        amountMilk += amount;
                        fillState = FillState.beans;
                        System.out.println("Write how many grams of coffee beans do you want to add:");
                        break;
                    case beans:
                        amountBeans += amount;
                        fillState = FillState.cups;
                        System.out.println("Write how many disposable cups of coffee do you want to add:");
                        break;
                    case cups:
                        amountCups += amount;
                        fillState = FillState.load;
                        break;
                }
            }
        }
        return bResult;
    }

    //Действие = забрать деньги
    private void doTake(){
        System.out.printf("I gave you $%s\n",amountMoney);
        amountMoney = 0;
    }

    //Основное действие
    public boolean doAction(String action) {
        boolean bResult = false;

        if (action != null) {
            if (state == State.menu) {
                try {
                    state = State.valueOf(action);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid action!");
                    bResult = true;
                }
            } else if ((state == State.buy) && (buyState == BuyState.nothing)) {
                if (action.equals("back")) {
                    buyState = BuyState.back;
                } else if (isNumeric(action)) {
                    int iTemp;
                    try {
                        iTemp = Integer.parseInt(action);
                        switch (iTemp) {
                            case 1:
                                buyState = BuyState.espresso;
                                break;
                            case 2:
                                buyState = BuyState.latte;
                                break;
                            case 3:
                                buyState = BuyState.cappuccino;
                                break;
                            default:
                                System.out.println("Invalid choose!");
                                bResult = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid choose!");
                        bResult = true;
                    }
                }
            }
        }

        if (bResult) {
            initState();
        } else {
            switch (state) {
                case menu:
                    System.out.println("Write action (buy, fill, take, remaining, exit):");
                    break;
                case buy:
                    doBuy();
                    break;
                case fill:
                    bResult = !doFill(action);
                    if (fillState == FillState.load) {
                        initState();
                        return doAction(null);
                    }
                    break;
                case take:
                    doTake();
                    initState();
                    return doAction(null);
                case remaining:
                    printState();
                    initState();
                    return doAction(null);
                case exit:
                    bResult = true;
                    break;
            }
        }

        return bResult;
    }
}

public class CoffeeMachine {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Machine machine = new Machine();
        String action;

        boolean checkExit;
        do {
            action = scanner.nextLine().trim().toLowerCase();
            checkExit = machine.doAction(action);
        } while (!checkExit);
    }
}