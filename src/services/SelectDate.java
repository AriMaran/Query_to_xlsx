package services;

import java.util.Scanner;

public class SelectDate {

    private Scanner sc;

    public SelectDate() {
    	
        sc = new Scanner(System.in);
    }

    public String getStartDateFromUser() {
    	
        System.out.print("Enter the start date(YYYYMMDD): ");
        return sc.nextLine().trim();
    }

    public String getEndDateFromUser() {
    	
        System.out.print("Enter the end date(YYYYMMDD): ");
        return sc.nextLine().trim();
    }

    public String updateQueryWithDates(String query, String startDate, String endDate) {
        return query.replace("start", startDate).replace("final", endDate);
    }
    

}
