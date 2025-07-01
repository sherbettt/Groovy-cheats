String str1 = "123";
String str2 = new String("123");
if (str1 == str2) {  // В Groovy лучше использовать == для сравнения значений
    println("equal");
} else {
    println("Not equal");
}


String str3 = "123";
String str4 = new String("123");

println(str3 == str4);      // Может быть true или false (зависит от оптимизации)
println(str3.equals(str4)); // Всегда true (точно сравнивает содержимое)


String str5 = "123";
String str6 = new String("123");

if (str5.equals(str6)) {  // Точно сравнивает содержимое
    println("equal");     // Напечатает "equal"
} else {
    println("Not equal");
}





















