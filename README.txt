https://github.com/BojanFlavius/Licenta

Pentru a putea crea un server o sa avem nevoie de aplicatia XAMPP din link-ul aceasta 
https://www.apachefriends.org/
Odata instalata trebuie sa intram la modulul Apache la actiunea config, sectiunea apache 
si trebuie gasita linia in care este scris Listen si schimbat numarul respectiv cu portul 
pe care vrem sa facem port forwarding(in cazul de fata este 3300)
In fisierul instalat veti gasi un director htdocs unde trebuie adaugat folderul findme cu 
scriptul PHP in interiorul lui.
O sa avem nevoie si de Composer pe care il puteti descarca cu ajutorul acestui link
https://getcomposer.org/download/
Dupa instalare in interiorul directorului htdocs vom da comanda composer init unde ne va 
crea directorul vendor care contine autoload.php pe care o sa il folosim in script si 
fisierul composer.json unde vom adauga 

{
    "require": {
        "kreait/firebase-php": "^5.26"
    }
}

Urmatorul pas este sa cream port forwarding in interiorul router-ului.
Pentru a intra in setarile router-ului va trebui sa intrati in cmd si acolo trebuia data 
comanda ipconfig pentru a ne gasi default Gateway. Odata ce avem adresa vom intra intr-un
motor de cautare si vom accesa adresa. (De obicei e 192.168.0.1)
Acolo va trebui sa intram la sectiunea de port forwarding si vom crea un nou server cu port-ul
scris in xampp.
In Xampp va trebui pornit serverul de la modulul Apache. Daca totul merge corect ar trebui sa poti 
verifica portul cu ajutorul adresei tale ip si port-ul selectat cu ajutorul site-urilor care ofera
aceasta functionalitate ca si acesta
https://portchecker.co/

Pentru a rula codul pentru placuta vom avea nevoie de echipamentul hardware si spatiul de lucru Arduino
pe care il putem instala cu ajutorul acestui link
https://www.arduino.cc/en/software
In interiorul aplicatiei Arduino trebuie instalata libraria "Adafruit FONA Library", iar apoi se poate 
incaraca codul pe placa.

Pentru a rula codul pentru aplicatia mobila vom avea nevoie de Android Studio care se poate descarca de aici
https://developer.android.com/studio
se instaleaza, se creeaza un telefon in emulator si se poate porni aplicatia.