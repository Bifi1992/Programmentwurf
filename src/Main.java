import java.util.ArrayList;

/**
 * TODO Rename {@link Interface} to Main, delete {@link Main}
 * Created by y.brisch on 17.05.17.
 */
public class Main {

  public static void main(String[] args) {
    javafx.application.Application.launch(Interface.class, args);
    /* SZ-Zahlenrätsel
    ArrayList<Integer> nums = new ArrayList();
    nums.add(3);
    nums.add(6);
    nums.add(9);
    nums.add(11);
    String[] ops = {"*", "/", "+", "-"};
    for (int i = 0; i < ops.length; i++) {
      for (int j = 0; j < ops.length; j++) {
        for (int k = 0; k < ops.length; k++) {
          for (int l = 0; l < nums.size(); l++) {
            ArrayList<Integer> numList = nums;
            ArrayList<Integer> chosenNums = new ArrayList<>();
            chosenNums.add(numList.get(l));
            numList.remove(l);
            for (int m = 0; m < numList.size(); m++) {
              chosenNums.add(numList.get(m));
              numList.remove(m);
              for (int n = 0; n < numList.size(); n++) {
                chosenNums.add(numList.get(n));
                numList.remove(n);
                chosenNums.add(numList.get(0));
                double result = chosenNums.get(0);
                StringBuilder sb = new StringBuilder();
                sb.append("((((").append(chosenNums.get(0));
                String[] chosenOps = {ops[i], ops[j], ops[k]};
                for (int o = 0; o < chosenOps.length; o++) {
                  switch (chosenOps[o]) {
                    case "*":
                      result = result * chosenNums.get(o+1);
                      sb.append("*");
                      break;
                    case "/":
                      result = result / chosenNums.get(o+1);
                      sb.append("/");
                      break;
                    case "+":
                      result = result + chosenNums.get(o+1);
                      sb.append("+");
                      break;
                    case "-":
                      result = result - chosenNums.get(o+1);
                      sb.append("-");
                      break;
                  }
                  sb.append(chosenNums.get(o+1)).append(")");
                }
                if (result == 24) {
                  System.out.println(sb.toString() + " = " + result);
                }
                numList.add(n, chosenNums.get(2));
                chosenNums.remove(3);
                chosenNums.remove(2);
              }
              numList.add(m, chosenNums.get(1));
              chosenNums.remove(1);
            }
            numList.add(l, chosenNums.get(0));
            chosenNums.remove(0);
          }
        }
      }
    }*/
  }
}
