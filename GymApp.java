import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

class Member{
    int memberID;
    String name;
    String email;
    String phone;
    static int idCounter = 1;

    public Member(String name, String email, String phone) {
        this.memberID = idCounter++;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String toString() {
        return memberID + ": " + name + " (" + email + ", " + phone + ")";
    }
}

class MembershipPlan{
    String planName;
    int durationMonths;
    double fee;

    public MembershipPlan(String planName, int durationMonths, double fee){
        this.planName = planName;
        this.durationMonths = durationMonths;
        this.fee = fee;
    }

    String getPlanDetails() {
        return planName + " - " + durationMonths + " months - $" + fee;
    }
}

class Membership{
    Member member;
    MembershipPlan plan;
    Date startDate;
    Date endDate;
    boolean active;

    public Membership(Member m, MembershipPlan p) {
        Calendar cal;

        member = m;
        plan = p;
        startDate = new Date();

        cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, plan.durationMonths);
        endDate = cal.getTime();

        active = true;
    }

    double calculateFee(){
        return plan.fee;
    }

    String checkStatus(){
        Date today;

        today = new Date();

        if(today.after(endDate)){
            active = false;
            return "Expired";
        }

        active = true;
        return "Active";
    }

    String getDetails(){
        return member +
                "\nPlan: " + plan.getPlanDetails() +
                "\nFee: $" + calculateFee() +
                "\nStart Date: " + startDate +
                "\nEnd Date: " + endDate +
                "\nStatus: " + checkStatus();
    }
}

class GymSystem{
    ArrayList<Member> members;
    ArrayList<MembershipPlan> plans;
    ArrayList<Membership> memberships;
    static GymSystem instance;

    private GymSystem(){
        members = new ArrayList<>();
        plans = new ArrayList<>();
        memberships = new ArrayList<>();

        plans.add(new MembershipPlan("1 Month", 1, 50));
        plans.add(new MembershipPlan("3 Months", 3, 140));
        plans.add(new MembershipPlan("6 Months", 6, 270));
        plans.add(new MembershipPlan("12 Months", 12, 500));
    }

    static GymSystem getInstance(){
        if(instance == null){
            instance = new GymSystem();
        }
        return instance;
    }

    Member registerMember(String name, String email, String phone){
        int i;
        Member m;
        Member newM;

        if(name == null || email == null || phone == null){
            return null;
        }

        name = name.trim();
        email = email.trim();
        phone = phone.trim();

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty()){
            JOptionPane.showMessageDialog(null, "All fields required");
            return null;
        }

        for(i = 0; i < members.size(); i++){
            m = members.get(i);
            if(m.email.equalsIgnoreCase(email) || m.phone.equals(phone)){
                JOptionPane.showMessageDialog(null, "Email or phone already exists");
                return null;
            }
        }

        newM = new Member(name, email, phone);
        members.add(newM);
        return newM;
    }

    boolean updateMember(int id, String name, String email, String phone){
        int i;
        Member member;
        Member m;

        if(name == null || email == null || phone == null){
            return false;
        }

        name = name.trim();
        email = email.trim();
        phone = phone.trim();

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty()){
            JOptionPane.showMessageDialog(null, "All fields required");
            return false;
        }

        member = findMemberByID(id);

        if(member == null){
            JOptionPane.showMessageDialog(null, "Member not found");
            return false;
        }

        for(i = 0; i < members.size(); i++){
            m = members.get(i);
            if(m.memberID != id){
                if(m.email.equalsIgnoreCase(email) || m.phone.equals(phone)){
                    JOptionPane.showMessageDialog(null, "Email or phone already exists");
                    return false;
                }
            }
        }

        member.name = name;
        member.email = email;
        member.phone = phone;
        return true;
    }

    Member findMemberByID(int id){
        int i;
        Member m;

        for(i = 0; i < members.size(); i++){
            m = members.get(i);
            if(m.memberID == id){
                return m;
            }
        }

        return null;
    }

    Membership assignMembership(Member member, MembershipPlan plan){
        Membership oldMembership;
        Membership m;

        if(member == null || plan == null){
            return null;
        }

        oldMembership = findMembershipByMemberID(member.memberID);
        if(oldMembership != null){
            memberships.remove(oldMembership);
        }

        m = new Membership(member, plan);
        memberships.add(m);
        return m;
    }

    Membership findMembershipByMemberID(int id){
        Membership m;

        for(int i = 0; i < memberships.size(); i++){
            m = memberships.get(i);
            if(m.member.memberID == id){
                return m;
            }
        }

        return null;
    }

    MembershipPlan findPlanByDetails(String details){
        int j;
        MembershipPlan p;

        if(details == null){
            return null;
        }

        for(j = 0; j < plans.size(); j++){
            p = plans.get(j);
            if(p.getPlanDetails().equals(details)){
                return p;
            }
        }

        return null;
    }

    boolean addPlan(String name, int dur, double fee){
        int i;
        MembershipPlan p;
        MembershipPlan newPlan;

        if(name == null){
            return false;
        }

        name = name.trim();

        if(name.isEmpty() || dur <= 0 || fee <= 0){
            JOptionPane.showMessageDialog(null, "Enter valid plan details");
            return false;
        }

        for(i = 0; i < plans.size(); i++){
            p = plans.get(i);
            if(p.planName.equalsIgnoreCase(name) && p.durationMonths == dur){
                JOptionPane.showMessageDialog(null, "Plan already exists");
                return false;
            }
        }

        newPlan = new MembershipPlan(name, dur, fee);
        plans.add(newPlan);
        return true;
    }

    void showAllMemberships(){
        int i;
        String all;
        Membership m;

        if(memberships.isEmpty()){
            JOptionPane.showMessageDialog(null, "No memberships found");
            return;
        }

        all = "";

        for(i = 0; i < memberships.size(); i++){
            m = memberships.get(i);
            all = all + m.getDetails() + "\n\n";
        }

        JOptionPane.showMessageDialog(null, all);
    }
}

public class GymApp {
    JFrame frame;
    GymSystem system;
    JButton regBtn;
    JButton assignBtn;
    JButton displayBtn;
    JButton updateBtn;
    JButton createPlanBtn;
    JButton showAllBtn;
    JButton exitBtn;

    GymApp(){
        system = GymSystem.getInstance();
        frame = new JFrame("Gym Management System");
        frame.setSize(450, 500);
        frame.setLayout(new GridLayout(7, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        regBtn = new JButton("Register Member");
        assignBtn = new JButton("Assign Membership");
        displayBtn = new JButton("Display Membership");
        updateBtn = new JButton("Update Member Info");
        createPlanBtn = new JButton("Create Plan");
        showAllBtn = new JButton("Show All Memberships");
        exitBtn = new JButton("Exit");

        frame.add(regBtn);
        frame.add(createPlanBtn);
        frame.add(assignBtn);
        frame.add(displayBtn);
        frame.add(updateBtn);
        frame.add(showAllBtn);
        frame.add(exitBtn);

        regBtn.addActionListener(e -> {
            String name;
            String email;
            String phone;
            Member m;

            name = JOptionPane.showInputDialog("Enter name:");
            if(name == null){
                return;
            }

            email = JOptionPane.showInputDialog("Enter email:");
            if(email == null){
                return;
            }

            phone = JOptionPane.showInputDialog("Enter phone:");
            if(phone == null){
                return;
            }

            m = system.registerMember(name, email, phone);

            if(m != null){
                JOptionPane.showMessageDialog(null, "Registered: " + m);
            }
        });

        createPlanBtn.addActionListener(e -> {
            String name;
            String durText;
            String feeText;
            int dur;
            double fee;
            boolean added;

            name = JOptionPane.showInputDialog("Plan name:");
            if(name == null){
                return;
            }

            dur = 0;
            fee = 0;

            try{
                durText = JOptionPane.showInputDialog("Months:");
                if(durText == null){
                    return;
                }
                dur = Integer.parseInt(durText);

                feeText = JOptionPane.showInputDialog("Fee:");
                if(feeText == null){
                    return;
                }
                fee = Double.parseDouble(feeText);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Enter valid numbers");
                return;
            }

            added = system.addPlan(name, dur, fee);

            if(added){
                JOptionPane.showMessageDialog(null, "Plan added successfully");
            }
        });

        assignBtn.addActionListener(e -> {
            String idText;
            int id;
            Member member;
            String[] options;
            int j;
            String sel;
            MembershipPlan plan;
            Membership membership;

            id = 0;

            try{
                idText = JOptionPane.showInputDialog("Member ID:");
                if(idText == null){
                    return;
                }
                id = Integer.parseInt(idText);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Enter a valid ID");
                return;
            }

            member = system.findMemberByID(id);

            if(member == null){
                JOptionPane.showMessageDialog(null, "Member not found");
                return;
            }

            options = new String[system.plans.size()];

            for(j = 0; j < system.plans.size(); j++){
                options[j] = system.plans.get(j).getPlanDetails();
            }

            sel = (String) JOptionPane.showInputDialog(
                    null,
                    "Select plan:",
                    "Plans",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            plan = system.findPlanByDetails(sel);
            membership = system.assignMembership(member, plan);

            if(membership != null){
                JOptionPane.showMessageDialog(null, "Membership assigned\nStatus: " + membership.checkStatus());
            }
        });

        displayBtn.addActionListener(e -> {
            String idText;
            int id;
            Membership m;

            id = 0;

            try{
                idText = JOptionPane.showInputDialog("Member ID:");
                if(idText == null){
                    return;
                }
                id = Integer.parseInt(idText);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Enter a valid ID");
                return;
            }

            m = system.findMembershipByMemberID(id);

            if(m != null){
                JOptionPane.showMessageDialog(null, m.getDetails());
            }else{
                JOptionPane.showMessageDialog(null, "Membership not found");
            }
        });

        updateBtn.addActionListener(e -> {
            String idText;
            int id;
            Member member;
            String name;
            String email;
            String phone;
            boolean updated;

            id = 0;

            try{
                idText = JOptionPane.showInputDialog("Member ID:");
                if(idText == null){
                    return;
                }
                id = Integer.parseInt(idText);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Enter a valid ID");
                return;
            }

            member = system.findMemberByID(id);

            if(member == null){
                JOptionPane.showMessageDialog(null, "Member not found");
                return;
            }

            name = JOptionPane.showInputDialog("New name:", member.name);
            if(name == null){
                return;
            }

            email = JOptionPane.showInputDialog("New email:", member.email);
            if(email == null){
                return;
            }

            phone = JOptionPane.showInputDialog("New phone:", member.phone);
            if(phone == null){
                return;
            }

            updated = system.updateMember(id, name, email, phone);

            if(updated){
                JOptionPane.showMessageDialog(null, "Member updated successfully");
            }
        });

        showAllBtn.addActionListener(e -> {
            system.showAllMemberships();
        });

        exitBtn.addActionListener(e -> {
            System.exit(0);
        });

        frame.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(GymApp::new);
    }
}
