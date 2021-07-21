package com.as;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Form extends JFrame {
    //Pola do przechowywania informacji o pracownikach
    private int licznik = 0;
    private JTextField IDtext = new JTextField();
    private JTextField FirstNameText = new JTextField();
    private JTextField LastNameText = new JTextField();
    private JTextField AddressText = new JTextField();
    private JTextField BirthDateText = new JTextField();
    private JTextField EmployerNameText = new JTextField();
    private JTextField EmployementDateText = new JTextField();
    private JTextField SalaryText = new JTextField();
    private JTextField MoneyText = new JTextField();
    private JComboBox JobtitleText = new JComboBox();
    private JComboBox Szukaj = new JComboBox();
    private JTextField SzukajText = new JTextField();
    private EmployeesL currentEmployee;

    //Pola dla przeglądania twarzy wygenerowanych pracowników
    private int opcja_dodawania = 0;
    private FaceContainer currentTwarze;

    //Pola dla "gry" wielowątkowej
    private static boolean[] table = new boolean[5];
    private JPanel[] gameanimation = new JPanel[5];
    private JPanel tabel = new JPanel();
    private JPanel[] game = new JPanel[5];
    private JPanel[] names = new JPanel[5];
    private JTextField[] gracz1name = new JTextField[5];
    private JTextField[] gracz2name = new JTextField[5];
    private JPanel[] gametablepar = new JPanel[5];
    private JTextArea[] gametable = new JTextArea[5];
    private boolean gametableparbussy[] = new boolean[5];

    Form(EmployeesL tablica) {
        //Ogólne ustawienia panelu

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        JPanel cPanel1 = new JPanel();
        cPanel1.setLayout(new BorderLayout(10, 10));
        JPanel cPanel2 = new JPanel();
        cPanel2.setLayout(new BorderLayout(10, 10));
        JPanel cPanel3 = new JPanel();
        cPanel3.setLayout(new BorderLayout(10, 10));
        tabbedPane.addTab("Edit employee", null, cPanel1, null);
        tabbedPane.addTab("Show Face", null, cPanel2, null);
        tabbedPane.addTab("Games", null, cPanel3, null);
        add(tabbedPane, BorderLayout.CENTER);

        currentEmployee = tablica;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        setSize(1350, 650);

        // Opcje edycji pracownika, u góry pod nawgiacją

        JPanel gora = new JPanel();
        JButton Add = new JButton("Add");
        Add.addActionListener(e -> {
            if (opcja_dodawania == 0) {
                licznik = currentEmployee.arrayList.size();
                currentEmployee.arrayList.add(new Employees());
                IDtext.setText(licznik + 1 + "");
                FirstNameText.setText(null);
                LastNameText.setText(null);
                AddressText.setText(null);
                BirthDateText.setText(null);
                EmployerNameText.setText(null);
                EmployementDateText.setText(null);
                SalaryText.setText(null);
                JobtitleText.setToolTipText(null);
                MoneyText.setText(null);
                opcja_dodawania = 1;
            }
        });
        gora.add(Add);

        JButton Save = new JButton("Save"); //zapis do pliku
        Save.addActionListener(e -> {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employeesL.dat"))) {
                out.writeObject(currentEmployee);
            } catch (FileNotFoundException er) {
                er.printStackTrace();
            } catch (IOException er) {
                er.printStackTrace();
            }
        });
        gora.add(Save);
        JButton Write = new JButton("Write"); //zapis danych do tablicy. modyfikacja, dodawanie itp.
        Write.addActionListener(e -> {
            if (FirstNameText.getText() != null) {
                if (LastNameText.getText() != null) {
                    if (AddressText.getText() != null) {
                        if (BirthDateText.getText() != null) {
                            if (EmployerNameText.getText() != null) {
                                if (EmployementDateText.getText() != null) {
                                    if (SalaryText.getText() != null) {
                                        currentEmployee.arrayList.get(licznik).setId(Integer.parseInt(IDtext.getText()));
                                        currentEmployee.arrayList.get(licznik).dodajI();
                                        currentEmployee.arrayList.get(licznik).setFirstname(FirstNameText.getText());
                                        currentEmployee.arrayList.get(licznik).setSurname(LastNameText.getText());
                                        currentEmployee.arrayList.get(licznik).setAddressLine(AddressText.getText());
                                        currentEmployee.arrayList.get(licznik).setBirthDate(BirthDateText.getText());
                                        currentEmployee.arrayList.get(licznik).setEmployerName(EmployerNameText.getText());
                                        currentEmployee.arrayList.get(licznik).setEmployementDate(EmployementDateText.getText());
                                        BigDecimal payment = new BigDecimal(SalaryText.getText());
                                        payment.round(MathContext.DECIMAL32);
                                        currentEmployee.arrayList.get(licznik).setSalary(payment);
                                        currentEmployee.arrayList.get(licznik).setJobtitle(JobtitleText.getSelectedItem().toString());
                                        if (MoneyText != null)
                                            currentEmployee.arrayList.get(licznik).setMoney(Integer.parseInt(MoneyText.getText()));
                                        else currentEmployee.arrayList.get(licznik).setMoney(0);
                                        updateFields();
                                        opcja_dodawania = 0;

                                    } else System.out.println("Musisz podac pensję");

                                } else System.out.println("Musisz podac date zatrudnienia");
                            } else System.out.println("Musisz podac nazwę zatrudniającego urodzenia");
                        } else System.out.println("Musisz podac date urodzenia");
                    } else System.out.println("Musisz podac adres");
                } else System.out.println("Musisz podac nazwisko");
            } else System.out.println("Musisz podac imie");
        });
        gora.add(Write);
        JButton Find = new JButton("Find");
        Find.addActionListener(e -> {
            String Kryterium = Szukaj.getSelectedItem().toString();
            String WyszukiwanaFraza = SzukajText.getText();
            boolean found = false;
            switch (Kryterium) {
                case "id":
                    for (int ip = licznik + 1; ip < currentEmployee.arrayList.size(); ip++) {
                        String obecnawarotsc = currentEmployee.arrayList.get(ip).idtoString();
                        if (obecnawarotsc.contains(WyszukiwanaFraza)) {
                            licznik = ip;
                            updateFields();
                            found = true;
                            break;
                        }
                    }
                    if (found == false)
                        for (int ip = 0; ip <= licznik; ip++) {
                            String obecnawarotsc = currentEmployee.arrayList.get(ip).idtoString();
                            if (obecnawarotsc.contains(WyszukiwanaFraza)) {
                                licznik = ip;
                                updateFields();
                                found = true;
                                break;
                            }
                        }
                    if (found == false) System.out.println("Nie ma w bazie szukanej wartości");
                    break;
                case "nazwisko":
                    for (int io = licznik + 1; io < currentEmployee.arrayList.size(); io++) {
                        if (currentEmployee.arrayList.get(io).getSurname().contains(WyszukiwanaFraza)) {
                            licznik = io;
                            updateFields();
                            found = true;
                            break;
                        }
                    }
                    if (found == false)
                        for (int io = 0; io <= licznik; io++) {
                            if (currentEmployee.arrayList.get(io).getSurname().contains(WyszukiwanaFraza)) {
                                licznik = io;
                                updateFields();
                                found = true;
                                break;
                            }
                        }
                    if (found == false) System.out.println("Nie ma w bazie szukanej wartości");
                    break;
                case "data urodzenia":
                    for (int io = licznik + 1; io < currentEmployee.arrayList.size(); io++) {
                        if (currentEmployee.arrayList.get(io).getBirthDate().contains(WyszukiwanaFraza)) {
                            licznik = io;
                            updateFields();
                            found = true;
                            break;
                        }
                    }
                    if (found == false)
                        for (int io = 0; io <= licznik; io++) {
                            if (currentEmployee.arrayList.get(io).getBirthDate().contains(WyszukiwanaFraza)) {
                                licznik = io;
                                updateFields();
                                found = true;
                                break;
                            }
                        }
                    if (found == false) System.out.println("Nie ma w bazie szukanej wartości");
                    break;
                case "zatrudnienia":
                    for (int io = licznik + 1; io < currentEmployee.arrayList.size(); io++) {
                        if (currentEmployee.arrayList.get(io).getJobtitle().contains(WyszukiwanaFraza)) {
                            licznik = io;
                            updateFields();
                            found = true;
                            break;
                        }
                    }
                    if (found == false)
                        for (int io = 0; io <= licznik; io++) {
                            if (currentEmployee.arrayList.get(io).getJobtitle().contains(WyszukiwanaFraza)) {
                                licznik = io;
                                updateFields();
                                found = true;
                                break;
                            }
                        }
                    if (found == false) System.out.println("Nie ma w bazie szukanej wartości");
                    break;
                default:
                    System.out.println("Nie wybrano kryterium do szukania");
            }
        });
        gora.add(Find);
        Szukaj.addItem("id");
        Szukaj.addItem("nazwisko");
        Szukaj.addItem("data urodzenia");
        Szukaj.addItem("zatrudnienia");
        gora.add(Szukaj);

        SzukajText.setText("");
        SzukajText.setColumns(15);
        gora.add(SzukajText);
        cPanel1.add(gora, BorderLayout.PAGE_START);

        //Nawigacja po pracownikach

        JPanel nawigacja = new JPanel();
        nawigacja.setLayout(new BoxLayout(nawigacja, BoxLayout.PAGE_AXIS));
        JButton First = new JButton("<<");
        First.addActionListener(e -> {
            licznik = 0;
            updateFields();
            JPanel face = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);
                }
            };
            cPanel2.add(face, BorderLayout.CENTER);
            revalidate();
        });
        nawigacja.add(First);

        JButton Last = new JButton(">>");
        Last.addActionListener(e -> {
            licznik = currentEmployee.arrayList.size() - 1;
            updateFields();
            JPanel face = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);
                }
            };
            cPanel2.add(face, BorderLayout.CENTER);
            revalidate();
        });
        nawigacja.add(Last);

        JButton Back = new JButton("<");
        Back.addActionListener(e -> {
            if (licznik > 0) {
                licznik--;
                updateFields();
                JPanel face = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);

                    }
                };
                cPanel2.add(face, BorderLayout.CENTER);
                revalidate();
            }
        });
        nawigacja.add(Back);

        JButton Forward = new JButton(">");
        Forward.addActionListener(e -> {
            if (licznik < currentEmployee.arrayList.size() - 1) {
                licznik++;
                updateFields();
                JPanel face = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);

                    }
                };
                cPanel2.add(face, BorderLayout.CENTER);
                revalidate();
            }
        });
        nawigacja.add(Forward);
        cPanel1.add(nawigacja, BorderLayout.LINE_START);


        //Informacje o pracownikach

        JPanel etykiety = new JPanel();
        etykiety.setLayout(new BoxLayout(etykiety, BoxLayout.PAGE_AXIS));

        JPanel ide = new JPanel();
        ide.setLayout(new FlowLayout());
        JLabel ID = new JLabel("ID");
        ide.add(ID);
        IDtext.setEditable(false);
        IDtext.setText(tablica.arrayList.get(licznik).idtoString());
        IDtext.setColumns(33);
        ide.add(IDtext);
        etykiety.add(ide);

        JPanel imie = new JPanel();
        imie.setLayout(new FlowLayout());
        JLabel FirstName = new JLabel("First Name");
        imie.add(FirstName);
        FirstNameText.setText(tablica.arrayList.get(licznik).getFirstname());
        FirstNameText.setColumns(29);
        imie.add(FirstNameText);
        etykiety.add(imie);

        JPanel nazwisko = new JPanel();
        nazwisko.setLayout(new FlowLayout());
        JLabel LastName = new JLabel("Last Name");
        nazwisko.add(LastName);
        LastNameText.setText(tablica.arrayList.get(licznik).getSurname());
        LastNameText.setColumns(29);
        nazwisko.add(LastNameText);
        etykiety.add(nazwisko);

        JPanel adres = new JPanel();
        adres.setLayout(new FlowLayout());
        JLabel Address = new JLabel("Address");
        adres.add(Address);
        AddressText.setText(tablica.arrayList.get(licznik).getAddressLine());
        AddressText.setColumns(30);
        adres.add(AddressText);
        etykiety.add(adres);

        JPanel urodziny = new JPanel();
        urodziny.setLayout(new FlowLayout());
        JLabel BirthDate = new JLabel("Birth Date");
        urodziny.add(BirthDate);
        BirthDateText.setText(tablica.arrayList.get(licznik).getBirthDate());
        BirthDateText.setColumns(29);
        urodziny.add(BirthDateText);
        etykiety.add(urodziny);

        JPanel praco = new JPanel();
        praco.setLayout(new FlowLayout());
        JLabel EmployerName = new JLabel("Employer Name");
        praco.add(EmployerName);
        EmployerNameText.setText(tablica.arrayList.get(licznik).getEmployerName());
        EmployerNameText.setColumns(26);
        praco.add(EmployerNameText);
        etykiety.add(praco);

        JPanel zatrudniony = new JPanel();
        zatrudniony.setLayout(new FlowLayout());
        JLabel EmployementDate = new JLabel("Employement Date");
        zatrudniony.add(EmployementDate);
        EmployementDateText.setText(tablica.arrayList.get(licznik).getEmployementDate());
        EmployementDateText.setColumns(25);
        zatrudniony.add(EmployementDateText);
        etykiety.add(zatrudniony);

        JPanel pensja = new JPanel();
        pensja.setLayout(new FlowLayout());
        JLabel Salary = new JLabel("Salary");
        pensja.add(Salary);
        SalaryText.setText(tablica.arrayList.get(licznik).getSalary().round(MathContext.DECIMAL32).toString());
        SalaryText.setColumns(31);
        pensja.add(SalaryText);
        etykiety.add(pensja);

        JPanel pieniadze = new JPanel();
        pieniadze.setLayout(new FlowLayout());
        JLabel money = new JLabel("Money");
        pieniadze.add(money);
        MoneyText.setText(tablica.arrayList.get(licznik).getMoney().toString());
        MoneyText.setColumns(31);
        pieniadze.add(MoneyText);
        etykiety.add(pieniadze);

        JPanel tytul = new JPanel();
        tytul.setLayout(new FlowLayout());
        JLabel Jobtitle = new JLabel("Job title");
        tytul.add(Jobtitle);
        for (int q = 0; q < currentEmployee.arrayList.size(); q++)
            JobtitleText.addItem(tablica.arrayList.get(q).getJobtitle());
        tytul.add(JobtitleText);
        etykiety.add(tytul);

        cPanel1.add(etykiety, BorderLayout.CENTER);


        //Zapis/odczyt twarzy

        JPanel gora2 = new JPanel();

        JButton Save2 = new JButton("Save");
        Save2.addActionListener(e -> {
            try {
                FaceContainer faceContainer = new FaceContainer();
                for (var i = 0; i < currentEmployee.arrayList.size(); i++) {
                    byte[] bytes = currentEmployee.arrayList.get(i).getImageButByte();
                    ByteArrayWrapper wrapper = new ByteArrayWrapper(bytes);
                    faceContainer.getWrappers().add(wrapper);
                }
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imgs.dat"))) {
                    out.writeObject(faceContainer);
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
        gora2.add(Save2);

        JButton Read2 = new JButton("Read");
        Read2.addActionListener(e -> {
            try {
                int j = 0;
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("imgs.dat"));
                currentTwarze = (FaceContainer) in.readObject();
                ArrayList<ByteArrayWrapper> wrappers = currentTwarze.getWrappers();
                currentTwarze.setBufferedImages(new ArrayList<>());
                for (var wrapper : wrappers) {
                    if (licznik + j < currentEmployee.arrayList.size()) {
                        currentEmployee.arrayList.get(licznik + j).setemployeeFace(currentTwarze.debuffer(wrapper.getImage()));
                        currentEmployee.arrayList.get(licznik + j).setImageButByte(wrapper.getImage());
                    } else {
                        System.out.println("Wczytano " + j + " obrazów.");
                        break;
                    }
                    j++;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (ClassNotFoundException ce) {
                ce.printStackTrace();
            }
        });
        gora2.add(Read2);
        cPanel2.add(gora2, BorderLayout.PAGE_START);


        // Pole na twarz

        JPanel faceFirst = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);
            }
        };
        cPanel2.add(faceFirst, BorderLayout.CENTER);
        revalidate();


        //Przegladanie twarzy

        JPanel nawigacja2 = new JPanel();
        nawigacja2.setLayout(new BoxLayout(nawigacja2, BoxLayout.PAGE_AXIS));
        JButton First2 = new JButton("<<");
        First2.addActionListener(e -> {
            licznik = 0;
            updateFields();
            JPanel face = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);

                }
            };
            cPanel2.add(face, BorderLayout.CENTER);
            revalidate();
        });
        nawigacja2.add(First2);

        JButton Last2 = new JButton(">>");
        Last2.addActionListener(e -> {
            licznik = currentEmployee.arrayList.size() - 1;
            updateFields();
            JPanel face = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);

                }
            };
            cPanel2.add(face, BorderLayout.CENTER);
            revalidate();
        });
        nawigacja2.add(Last2);

        JButton Back2 = new JButton("<");
        Back2.addActionListener(e -> {
            if (licznik > 0) {
                licznik--;
                updateFields();
                JPanel face = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);

                    }
                };
                cPanel2.add(face, BorderLayout.CENTER);
                revalidate();
            }
        });
        nawigacja2.add(Back2);
        JButton Forward2 = new JButton(">");
        Forward2.addActionListener(e -> {
            if (licznik < currentEmployee.arrayList.size() - 1) {
                licznik++;
                updateFields();
                JPanel face = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.drawImage(currentEmployee.arrayList.get(licznik).getEmployeeFace(), 0, 0, null);

                    }
                };
                cPanel2.add(face, BorderLayout.CENTER);
                revalidate();
            }
        });
        nawigacja2.add(Forward2);
        cPanel2.add(nawigacja2, BorderLayout.LINE_START);

        //Gra menu

        JPanel nawigacja3 = new JPanel();
        nawigacja3.setLayout(new FlowLayout());
        JButton start = new JButton("Play");

        nawigacja3.add(start);
        cPanel3.add(nawigacja3, BorderLayout.NORTH);

        //Gra obrazy do animacji

        File[] file = new File[24];
        BufferedImage[] imageArt = new BufferedImage[24];
        for (int p = 1; p <= 23; p++) {

            try {
                file[p] = new File("imgani/Moneta" + p + ".png");
                imageArt[p] = ImageIO.read(file[p]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Gra panele

        JScrollPane[] gametablescroll = new JScrollPane[5];
        for (int i = 0; i < 5; i++) {
            tabel.setLayout(new FlowLayout());
            game[i] = new JPanel();
            game[i].setLayout(new BorderLayout());
            names[i] = new JPanel();
            names[i].setLayout(new FlowLayout());
            gracz1name[i] = new JTextField();
            gracz1name[i].setEditable(false);
            gracz1name[i].setColumns(10);
            names[i].add(gracz1name[i]);
            gracz2name[i] = new JTextField();
            gracz2name[i].setEditable(false);
            gracz2name[i].setColumns(10);
            names[i].add(gracz2name[i]);
            game[i].add(names[i], BorderLayout.NORTH);
            gametablepar[i] = new JPanel(new GridLayout(1, 2));
            gametable[i] = new JTextArea();
            DefaultCaret caret = (DefaultCaret) gametable[i].getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            gametable[i].setEditable(false);
            gametable[i].setLineWrap(true);
            gametablescroll[i] = new JScrollPane(gametable[i]);
            gametablescroll[i].setPreferredSize(new Dimension(200, 200));
            gametablepar[i].add(gametablescroll[i]);

            gameanimation[i] = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(imageArt[1], 0, 0, null);
                }
            };
            gametablepar[i].add(gameanimation[i]);
            gametableparbussy[i] = false;


            game[i].add(gametablepar[i]);
            tabel.add(game[i]);
        }

        //Gracze (bieże 20 pierwszych pracowników jako nowi gracze)

        Player[] GraczeLista = new Player[20];
        for (int p = 0; p < 20; p++) {
            GraczeLista[p] = new Player(currentEmployee.arrayList.get(p).getFirstname() + " " + currentEmployee.arrayList.get(p).getSurname(), currentEmployee.arrayList.get(p).getMoney(), currentEmployee.arrayList.get(p).getId());
        }

        cPanel3.add(tabel, BorderLayout.CENTER);

        //Zrównoleglona gra na pięciu polach

        start.addActionListener(e -> {
            for (int io = 0; io < 20; io++) {
                for (int jo = io + 1; jo < 20; jo++) {
                    int finalI = io;
                    int finalJ = jo;
                    new Thread(() -> {
                        int table_id = -1;
                        int g1 = finalI;
                        int g2 = finalJ;
                        synchronized (this) {
                            while (GraczeLista[g1].getZablokowany() || GraczeLista[g2].getZablokowany() || (table[0] && table[1] && table[2] && table[3] && table[4])) {
                                try {
                                    wait();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                            GraczeLista[g1].setZablokowany(true);
                            GraczeLista[g2].setZablokowany(true);
                            for (int tablei = 0; tablei < 5; tablei++) {
                                if (!table[tablei]) {
                                    table[tablei] = true;
                                    table_id = tablei;
                                    tablei = 5;
                                }
                            }
                        }
                        gametable[table_id].setText(null);
                        gracz1name[table_id].setText(GraczeLista[g1].getName());
                        gracz2name[table_id].setText(GraczeLista[g2].getName());
                        for (int p = 0; p < 5; p++) {
                            int kwota1 = GraczeLista[g1].getPieniadze();
                            int kwota2 = GraczeLista[g2].getPieniadze();

                            if (p == 0 && (kwota1 <= 0 || kwota2 <= 0)) {
                                synchronized (this) {
                                    gametable[table_id].setText(gametable[table_id].getText() + "\n Jeden z graczy nie jest w stanie grać:");
                                    gametable[table_id].setText(gametable[table_id].getText() + "\n Stan gracza " + GraczeLista[g1].getName() + ": " + GraczeLista[g1].getPieniadze());
                                    gametable[table_id].setText(gametable[table_id].getText() + "\n Stan gracza " + GraczeLista[g2].getName() + ": " + GraczeLista[g2].getPieniadze());
                                }
                                p = 5;
                            } else if (kwota1 <= 0) {
                                synchronized (this) {
                                    gametable[table_id].setText(gametable[table_id].getText() + "Gracz " + GraczeLista[g1].getName() + " stracił wszystkie pieniądze i zarazem przegrał");
                                }
                                p = 5;
                            } else if (kwota2 <= 0) {
                                synchronized (this) {
                                    gametable[table_id].setText(gametable[table_id].getText() + "Gracz " + GraczeLista[g2].getName() + " stracił wszystkie pieniądze i zarazem przegrał");
                                }
                                p = 5;
                            } else {
                                int los1 = (int) Math.floor(Math.random() * 10);
                                int los2 = (int) Math.floor(Math.random() * 10);

                                if (los1 >= 4 && los2 >= 4) {
                                    los(table_id, true, imageArt);
                                    synchronized (this) {
                                        gametableparbussy[table_id] = true;
                                    }
                                    while (gametableparbussy[table_id]) {
                                        try {
                                            Thread.sleep(1);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                        }
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                    los(table_id, true, imageArt);
                                    synchronized (this) {
                                        gametableparbussy[table_id] = true;
                                    }
                                    while (gametableparbussy[table_id]) {
                                        try {
                                            Thread.sleep(1);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                        }
                                    }
                                    synchronized (this) {
                                        gametable[table_id].setText(gametable[table_id].getText() + "\n Wylosowane zostały dwie reszki \n Gracz " + GraczeLista[g1].getName() + " wygrał");
                                        GraczeLista[g2].setPieniadze(kwota2 - 100);
                                        GraczeLista[g1].setPieniadze(kwota1 + 100);
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                } else if (los1 <= 3 && los2 <= 3) {
                                    los(table_id, false, imageArt);
                                    synchronized (this) {
                                        gametableparbussy[table_id] = true;
                                    }
                                    while (gametableparbussy[table_id]) {
                                        try {
                                            Thread.sleep(1);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                        }
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                    los(table_id, false, imageArt);
                                    synchronized (this) {
                                        gametableparbussy[table_id] = true;
                                    }
                                    while (gametableparbussy[table_id]) {
                                        try {
                                            Thread.sleep(1);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                        }
                                    }
                                    synchronized (this) {
                                        gametable[table_id].setText(gametable[table_id].getText() + "\n Wylosowane zostały dwa orły \n Gracz " + GraczeLista[g1].getName() + " wygrał");
                                        GraczeLista[g2].setPieniadze(kwota2 - 100);
                                        GraczeLista[g1].setPieniadze(kwota1 + 100);
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                } else {

                                    if (los1 <= 3) {
                                        los(table_id, false, imageArt);
                                        synchronized (this) {
                                            gametableparbussy[table_id] = true;
                                        }
                                        while (gametableparbussy[table_id]) {
                                            try {
                                                Thread.sleep(1);
                                            } catch (InterruptedException interruptedException) {
                                                interruptedException.printStackTrace();
                                            }
                                        }
                                        synchronized (this) {
                                            gametable[table_id].setText(gametable[table_id].getText() + "\n Gracz " + GraczeLista[g1].getName() + " wylosował orła");
                                        }

                                    } else if (los1 >= 4) {
                                        los(table_id, true, imageArt);
                                        synchronized (this) {
                                            gametableparbussy[table_id] = true;
                                        }
                                        while (gametableparbussy[table_id]) {
                                            try {
                                                Thread.sleep(1);
                                            } catch (InterruptedException interruptedException) {
                                                interruptedException.printStackTrace();
                                            }
                                        }
                                        synchronized (this) {
                                            gametable[table_id].setText(gametable[table_id].getText() + "\n Gracz " + GraczeLista[g1].getName() + " wylosował reszkę");
                                        }
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                    if (los2 <= 3) {
                                        los(table_id, false, imageArt);
                                        synchronized (this) {
                                            gametableparbussy[table_id] = true;
                                        }
                                        while (gametableparbussy[table_id]) {
                                            try {
                                                Thread.sleep(1);
                                            } catch (InterruptedException interruptedException) {
                                                interruptedException.printStackTrace();
                                            }
                                        }
                                        synchronized (this) {
                                            gametable[table_id].setText(gametable[table_id].getText() + "\n Gracz " + GraczeLista[g2].getName() + " wylosował orła");
                                        }
                                    } else if (los2 >= 4) {
                                        los(table_id, true, imageArt);
                                        synchronized (this) {
                                            gametableparbussy[table_id] = true;
                                        }
                                        while (gametableparbussy[table_id]) {
                                            try {
                                                Thread.sleep(1);
                                            } catch (InterruptedException interruptedException) {
                                                interruptedException.printStackTrace();
                                            }
                                        }
                                        synchronized (this) {
                                            gametable[table_id].setText(gametable[table_id].getText() + "\n Gracz " + GraczeLista[g2].getName() + " wylosował reszkę");
                                        }
                                    }
                                    synchronized (this) {
                                        gametable[table_id].setText(gametable[table_id].getText() + "\n Gracz " + GraczeLista[g2].getName() + "  wygrał");
                                        GraczeLista[g2].setPieniadze(kwota2 + 100);
                                        GraczeLista[g1].setPieniadze(kwota1 - 100);
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                }
                            }
                            synchronized (this) {
                                currentEmployee.arrayList.get(g1).setMoney(kwota1);
                                currentEmployee.arrayList.get(g2).setMoney(kwota2);
                                gametable[table_id].setText(gametable[table_id].getText() + "\n Stan gracza " + GraczeLista[g1].getName() + ": " + GraczeLista[g1].getPieniadze());
                                gametable[table_id].setText(gametable[table_id].getText() + "\n Stan gracza " + GraczeLista[g2].getName() + ": " + GraczeLista[g2].getPieniadze());
                                gametable[table_id].setText(gametable[table_id].getText() + "\n \n");
                            }
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        synchronized (this) {
                            gametable[table_id].setText(gametable[table_id].getText() + "\n \n");
                            GraczeLista[g1].setZablokowany(false);
                            GraczeLista[g2].setZablokowany(false);
                            table[table_id] = false;
                            notifyAll();

                        }
                    }).start();
                }
            }
        });

        //Menu nawigacji na samej górze, odczytywanie z pliku, wyjście

        JMenuBar nMenuBar = new JMenuBar();
        setJMenuBar(nMenuBar);

        JMenu fileMenu = new JMenu("File");
        nMenuBar.add(fileMenu);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> {
                    System.exit(JFrame.EXIT_ON_CLOSE);
                }
        );

        JMenuItem readMenuItem = new JMenuItem("Read");
        readMenuItem.addActionListener(e -> {
            UIManager.put("ProgressMonitor.progressText", "Test Progress");
            (new Thread(() -> {

                JFrame progress = new JFrame();
                progress.setDefaultCloseOperation(HIDE_ON_CLOSE);
                progress.setVisible(true);
                progress.setSize(300, 100);
                JPanel progress_panel = new JPanel();
                progress_panel.setLayout(new BorderLayout());
                JLabel obecne_wczytywanie = new JLabel("Reading employees");
                progress_panel.add(obecne_wczytywanie, BorderLayout.NORTH);
                JProgressBar obecny_progres = new JProgressBar();
                obecny_progres.setValue(0);
                obecny_progres.setStringPainted(true);
                progress_panel.add(obecny_progres, BorderLayout.CENTER);
                progress.add(progress_panel);
                ProgressMonitor progressMonitor = new ProgressMonitor(this,
                        "Reading employees",
                        "Percent", 0, 100);

                try (FileInputStream fis = new FileInputStream("employeesFirst.dat")) {
                    for (int i = 1; i <= 100; i++) {

                        String message = String.format("Completed %f%%.\n", ((double) i));
                        obecne_wczytywanie.setText(message);
                        obecny_progres.setValue(Double.valueOf(i).intValue());
                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                        } catch (InterruptedException el) {
                            System.err.println(el);
                        }
                    }
                    progress.setVisible(false);
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("employeesFirst.dat"));
                    currentEmployee = (EmployeesL) ois.readObject();
                } catch (IOException ie) {
                    ie.printStackTrace();
                } catch (ClassNotFoundException ce) {
                    ce.printStackTrace();
                }
            }
            )).start();
        });

        fileMenu.add(readMenuItem);
        fileMenu.add(exitMenuItem);
    }

    //Funkcja która odpowiada za animację w grze, wyrzucenie reszki lub orła
    void los(int id_table, boolean reszka, BufferedImage[] imageArt) {
        new Thread(() -> {
            if (reszka) {
                for (int rz = 0; rz <= 63; rz++) {
                    int finalRz = (rz % 23) + 1;
                    gametablepar[id_table].remove(gameanimation[id_table]);
                    gameanimation[id_table] = new JPanel() {
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.drawImage(imageArt[finalRz], 0, 0, null);
                        }
                    };
                    gametablepar[id_table].add(gameanimation[id_table]);
                    gametablepar[id_table].revalidate();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
                synchronized (this) {
                    gametableparbussy[id_table] = false;
                }

            } else {
                for (int rz = 0; rz <= 51; rz++) {
                    int finalRz = (rz % 23) + 1;
                    gametablepar[id_table].remove(gameanimation[id_table]);
                    gameanimation[id_table] = new JPanel() {
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.drawImage(imageArt[finalRz], 0, 0, null);
                        }
                    };
                    gametablepar[id_table].add(gameanimation[id_table]);
                    gametablepar[id_table].revalidate();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
                synchronized (this) {
                    gametableparbussy[id_table] = false;
                }
            }

        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    //Funkcja odpowiadająca za aktualizowanie pól pracowników
    void updateFields() {
        IDtext.setText(currentEmployee.arrayList.get(licznik).idtoString());
        FirstNameText.setText(currentEmployee.arrayList.get(licznik).getFirstname());
        LastNameText.setText(currentEmployee.arrayList.get(licznik).getSurname());
        AddressText.setText(currentEmployee.arrayList.get(licznik).getAddressLine());
        BirthDateText.setText(currentEmployee.arrayList.get(licznik).getBirthDate());
        EmployerNameText.setText(currentEmployee.arrayList.get(licznik).getEmployerName());
        EmployementDateText.setText(currentEmployee.arrayList.get(licznik).getEmployementDate());
        SalaryText.setText(currentEmployee.arrayList.get(licznik).getSalary().round(MathContext.DECIMAL32).toString());
        MoneyText.setText(currentEmployee.arrayList.get(licznik).getMoney().toString());
        var selection = currentEmployee.arrayList.get(licznik).getJobtitle();
        boolean found = false;
        for (var io = 0; io < JobtitleText.getItemCount(); io++) {
            if (selection == JobtitleText.getItemAt(io).toString()) {
                JobtitleText.setSelectedIndex(io);
                found = true;
            }
        }
        if (!found) {
            JobtitleText.addItem(selection);
            JobtitleText.setSelectedItem(selection);
        }
    }

    public static void main(String[] args) throws IOException {
        EmployeesL employeesList = new EmployeesL();
        for (int k = 0; k < 50; k++) {
            //Generacja pracowników
            Employees dod = new Employees(true);
            employeesList.add(dod);
        }
        //Zapisanie wygenerowanych pracowników do pliku
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employeesFirst.dat"))) {
            out.writeObject(employeesList);
        } catch (FileNotFoundException er) {
            er.printStackTrace();
        } catch (IOException er) {
            er.printStackTrace();
        }


        EventQueue.invokeLater(() -> new Form(employeesList));
    }
}