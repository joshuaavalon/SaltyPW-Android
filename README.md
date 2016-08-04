# ![Head](https://cdn.rawgit.com/joshuaavalon/SaltyPW-Android/master/app/src/main/res/mipmap-hdpi/ic_launcher.png) SaltyPW-Android
*Keep passwords safe. In your head.*

This project is Android implementation of [Salty.PW](https://salty.pw/) ([GitHub](https://github.com/SaltyPW/SaltyPW.github.io)).

##About
Salty.PW lets you have unique secure passwords without having to remember or store them anywhere.

All you need is a small space in your head for one password and the name of the application.

**Use one (strong) password for everything.**

Sounds dangerous, doesn't it? Not if you combine that one "base" password with a website name and pass it through an irreversible function to generate a unique password just for that particular website.

That generated password is what you'll enter into the password field when you login to reddit.com. Want to login to another website, say facebook.com? Just use the same base password and replace the website name with that of the new website, e.g. facebook.com.

If you try that now, you'll see that Salty.PW will generate a password different from the previous one. You'll also notice that each generated password, which is always 10 characters long, consists of both alphanumeric and non-alphanumeric characters. Meaning, the passwords Satly.PW generates are always difficult to crack.

Do you have to list down all those generated passwords in case you need to use them again? No. Nothing to store in a USB stick, a server in the cloud, or even a piece of paper. All you need is a small space in your head for that one password, the name of the website, and Salty.PW. It's simple, quick, and extremely secure.

##F.A.Q.
###Q: Do I still need a base password with digits, special characters and all that?

A: Kinda. Regardless of what your base password is, the generated password is immune to dictionary attacks unless the attacker is aware you are using Salty.PW. But while for most people that's an unlikely situation, it's better to be safe than sorry. Although instead of using digits and special characters we would recommend so called "XKCD passwords" (google it).

###Q: Is my password sent anywhere?

A: No. All computations are done in your phone. Neither your base password, nor the salt, nor the result are transmitted to any servers.

###Q: Why should I trust you that the above is true and will remain true in the future?

A: You shouldn't. Certainly not if your well-being depends on it. However, you can inspect the code or trust others to have inspected it. Furthermore, this website is itself hosted on GitHub so any modifications will be visible in the public commit history*. And if you want to be 100% future-proof, you can just fork the repository and build it yourself.

###Q: Once I started using Salty.PW am I bound to it forever? What happens if this site disappears one day?

A: You are bound to the algorithm (until you change your passwords), but not to this application. First of all, you can just fork the repository and build it yourself. Second, the algorithm is very simple (see below) and can be implemented in most mainstream programming languages in a matter of minutes. Finally, the entire site source code is on GitHub and will likely remain available for as long as GitHub is around and will hopefully be cloned and copied many times before the author gets hit by a bus.

###Q: What is the exact algorithm used by Salty.PW?

A: The base password and salt are concatenated together. Then a SHA-256* hash of the resulting string is computed. Finally, 128 most significant bits* of the hash are converted to base 95* using the following alphabet: ``0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_=!@#$%^&*()[]{}|;:,.<>/?`~ \'"+-``

*This can be changed in setting.
