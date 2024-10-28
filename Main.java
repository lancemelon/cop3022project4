import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	
    public static boolean isValidDouble(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true; // String is a valid double
        } catch (NumberFormatException e) {
            return false; // String is not a valid double
        }
    }
    
    public static boolean readData(String path, ArrayList<Asset> assets, ArrayList<String> invalid) {
		// Reading Asset Data CSV File
		// Store information in array of Assets
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Asset asset;

				switch (values.length) {
					case 3:
						if (isValidDouble(values[2])) {
							asset = new StableAsset(values[0], values[1], Double.parseDouble(values[2]));
							assets.add(asset);
						} else {
							asset = new StableAsset(values[0], values[1]);
							invalid.add(asset.toString());
						}
						break;
					case 4:
						if (isValidDouble(values[2]) && isValidDouble(values[3])) {
							asset = new Stock(values[0], values[1], Double.parseDouble(values[2]), Double.parseDouble(values[3]));
							assets.add(asset);
						} else {
							asset = new Stock(values[0], values[1]);
							invalid.add(asset.toString());
						}
						break;
					case 5:
						if (isValidDouble(values[2]) && isValidDouble(values[3]) && isValidDouble(values[4])) {
							asset = new Stock(values[0], values[1], Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4]));
							assets.add(asset);
						} else {
							asset = new Stock(values[0], values[1]);
							invalid.add(asset.toString());
						}
						break;
					default:
						System.out.println("Invalid asset data: " + line);
						break;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading data from file: " + e.getMessage());
			return false;
		}
		return true;
    }
    
    public static void displayAssets(ArrayList<Asset> assets) {
    	System.out.println("Available assets for investment");
    	System.out.println("-------------------------------");
    	for (Asset asset : assets) {
    		System.out.print("\t");
    		System.out.println(asset.toString());
    	}
    }
    
    public static Asset findAsset(ArrayList<Asset> assets, String ticker) {
    	for (Asset asset : assets) {
    		if (asset.getId().equals(ticker)) return asset;
    	}
    	return null;
    }
    
    public static String getTicker(Scanner scanner) {
        System.out.print("Enter a ticker symbol to invest in: ");
        return scanner.nextLine().toUpperCase();
    }

    public static void writeToFile(String ticker, int investment, int returnValue, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) { 
        	bw.write(String.format("| %-15s | %-17d | %-19d |", ticker, investment, returnValue));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static int getInvestment(Scanner scanner) {
        int investment = 0;
        while (true) {
            System.out.print("Enter the amount to invest in dollars: ");
            try {
                investment = scanner.nextInt(); 
                scanner.nextLine();
                return investment; 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); 
            }
        }
    }
    
    public static void writeTotal(int totalInvestment, int totalReturn, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write("+--------------+-----------------+--------------------+");
            bw.newLine();
            bw.write(String.format("| TOTAL       | %d             | %d                  |", totalInvestment, totalReturn));
            bw.newLine();
            bw.write("+--------------+-----------------+--------------------+");
        } catch (IOException e) {
            System.err.println("Error writing total to file: " + e.getMessage());
        }
    }

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		final String inputPath = "assetData.csv";
		final String outputPath = "portfolio.txt";
		
		ArrayList<Asset> assets = new ArrayList<>();
		ArrayList<String> invalid = new ArrayList<>();
		
		// Initialize the output file with headers
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
			bw.write("+--------------+-----------------+--------------------+");
			bw.newLine();
			bw.write("| ASSET SYMBOL | AMOUNT INVESTED | VALUE IN 10 YEARS |");
			bw.newLine();
		} catch (IOException e) {
			System.err.println("Error initializing output file: " + e.getMessage());
		}
		
		// Read asset data from CSV file goes wrong
		if (!readData(inputPath, assets, invalid)) {
			System.out.println("Failed to read asset data.");
			scanner.close();
			return; 
		}
		
		if(invalid.size() > 0) {
			System.out.print("THE DATA READ FOR");
			for(String i : invalid) {
				System.out.print(i);
			}
			System.out.println("WAS NOT VALID, SO IT WILL NOT BE AN AVAILBLE INVESTMENT!");
		}
		
		// Display assets and handle investments
		displayAssets(assets);
		
		int investment = 0;
		String ticker = "";
		int totalInvestment = 0;
		int totalReturn = 0;

		while (true) {
			investment = getInvestment(scanner); 
			if (investment < 0) break; 

			ticker = getTicker(scanner);
			Asset found = findAsset(assets, ticker);
			if (found == null) {
				System.out.printf("%s is not a valid investment option. Choose something else to invest in.\n", ticker);
				continue;
			}

			int re = found.calcReturn(investment);
			totalReturn += re;
			totalInvestment += investment;
			System.out.printf("Investing %d in %s has an expected future value of: %d\n", investment, ticker, re);

			// Write the investment details to the file
			writeToFile(ticker, investment, re, outputPath);
		}

		// Write total investment and return to the file
		writeTotal(totalInvestment, totalReturn, outputPath);

		scanner.close();
	}
}
