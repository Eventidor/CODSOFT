import java.util.Scanner;
public class CalulatorStud{
    public static void main(String[] args){
        Scanner sca = new Scanner(System.in);
        System.out.print("Enter the number of Subject: ");
        int numsub = sca.nextInt();
        double[] marks = new double[numsub];
        for(int i=0;i<numsub;i++){
            System.out.print("Enter marks obtained in subject "+(i+1)+"(out of 100): ");
            marks[i] = sca.nextDouble();
        }
     double totalMarks=0;
     for(double mark : marks){
        totalMarks += mark;
     }
     double averagePercentage=totalMarks/numsub;
     String grade;
    if (averagePercentage >= 90) {
        grade = "A+";
    } else if (averagePercentage >= 80) {
        grade = "A";
    } else if (averagePercentage >= 70) {
        grade = "B";
    } else if (averagePercentage >= 60) {
        grade = "C";
    } else if (averagePercentage >= 50) {
        grade = "D";
    } else {
        grade = "F";
    }
        System.out.println("Total Marks: " + totalMarks);
        System.out.printf("Average Percentage: %.2f%%\n", averagePercentage);
        System.out.println("Grade: " + grade);
        sca.close();
   }
}
