package codes.shubham.algorithms;

import java.util.*;

class BeautifulNumber {
  public int nextBeautifulNumber(int n) {
    int res = Integer.MAX_VALUE;
    int k = getNumberOfDigits(n);

    // next beautiful number can be of same number of digits and at max 1 more digit
    int maxDigits = k + 1;
    for (int i=k; i<=maxDigits; i++) {
      Set<Integer> balancedNumbers = generateBalancedNumbers(i);
      for (Integer balancedNumber : balancedNumbers) {
        if (balancedNumber > n) {
          res = Math.min(res, balancedNumber);
        }
      }
    }
    return res;
  }

  private int getNumberOfDigits(int n) {
    int k = 0;
    while (n > 0) {
      k++;
      n /= 10;
    }
    return k;
  }

  public Set<Set<Integer>> findBalancedDigits(Integer l) {
    Set<Set<Integer>> res = new HashSet<>();
    // base case
    Set<Integer> base = new HashSet<>();
    base.add(l);
    res.add(base);

    for (int i=1; i<=l/2; i++) {
      Set<Set<Integer>> subRes = findBalancedDigits(l-i);
      Set<Set<Integer>> subRes2 = findBalancedDigits(i);

      for (Set<Integer> l1 : subRes) {
        for (Set<Integer> l2 : subRes2) {
          // Remove if there is any convergence
          if (l1.containsAll(l2) || l2.containsAll(l1)) continue;

          Set<Integer> temp = new HashSet<>();
          temp.addAll(l1);
          temp.addAll(l2);
          res.add(temp);
        }
      }
    }
    return res;
  }

  public Set<Integer> generateBalancedNumbers(Integer l) {
    Set<Integer> res = new HashSet<>();
    Set<Set<Integer>> balancedDigits = findBalancedDigits(l);
    // Each digit k must be present k times in the number
    for (Set<Integer> balancedDigit : balancedDigits) {
      List<Integer> digits = new ArrayList<>();
      for (Integer digit : balancedDigit) {
        for (int i=0; i<digit; i++) {
          digits.add(digit);
        }
      }
      Collections.sort(digits);
      res.addAll(generateAllBalancedNumbersFromDigits(digits));

    }
    return res;
  }

  private Set<Integer> generateAllBalancedNumbersFromDigits(List<Integer> digits) {
    Set<Integer> res = new HashSet<>();
    generateAllBalancedNumbersFromDigitsHelper(digits, 0, res);
    return res;
  }

  private void generateAllBalancedNumbersFromDigitsHelper(List<Integer> digits, int i, Set<Integer> res) {
    if (i == digits.size()) {
      res.add(convertToNumber(digits));
      return;
    }

    for (int j=i; j<digits.size(); j++) {
      swap(digits, i, j);
      generateAllBalancedNumbersFromDigitsHelper(digits, i+1, res);
      swap(digits, i, j);
    }
  }

  private void swap(List<Integer> digits, int i, int j) {
    Integer temp = digits.get(i);
    digits.set(i, digits.get(j));
    digits.set(j, temp);
  }

  private Integer convertToNumber(List<Integer> digits) {
    Integer res = 0;
    for (Integer digit : digits) {
      res = res*10 + digit;
    }
    return res;
  }

  public static void main(String[] args) {
    BeautifulNumber beautifulNumber = new BeautifulNumber();
    System.out.println(beautifulNumber.nextBeautifulNumber(59866));
  }
}