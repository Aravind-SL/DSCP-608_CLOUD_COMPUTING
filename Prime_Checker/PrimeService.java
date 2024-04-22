package com.prime;

public class PrimeService {
    public void isPrime(int num) {
        boolean isPrime = true;
        for (int i = 2; i <= Math.sqrt(num); i++) {
        if (num % i == 0) {
        isPrime= false;
        break;
        }
    }
    if (isPrime) {
        System.out.println(num + " is a prime number.");
    } else {
        System.out.println(num + " is not a prime number.");
        }
    }
}