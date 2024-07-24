void main() {
  print(sqr(5));
  doSomething(() {
    var a = 1;
    var b = 3;
    return "$a + $b = ${a + b}";
  });
}

dynamic sqr(var a) => a * a;
void doSomething(String Function() func) {
  print(func());
}

class Human {
  final Human instance = new Human();
  static const double PI = 3.14;
}
