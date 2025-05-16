import java.io.*;
import java.util.*;

public class DataManagementSystem
{
    private static String filePath = "";

    /**
     * Finds and returns the entire item block for the specified item name.
     * Returns "Item not found or error occurred" if not found.
     */
    
    public static String findItem(String toFind)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.trim();
                if (line.startsWith("Item=") && line.substring(5, line.length() - 1).equalsIgnoreCase(toFind))
                {
                    List<String> allLines = readAllLines();
                    for (int i = 0; i < allLines.size(); i++)
                    {
                        String curr = allLines.get(i).trim();
                        if (curr.equals("Item=" + toFind + ";"))
                        {
                            int start = i;
                            while (start >= 0 && !allLines.get(start).trim().equals("{"))
                            {
                                start--;
                            }
                            int end = i;
                            while (end < allLines.size() && !allLines.get(end).trim().equals("}"))
                            {
                                end++;
                            }
                            if (start >= 0 && end < allLines.size())
                            {
                                StringJoiner result = new StringJoiner("\n");
                                for (int j = start; j <= end; j++)
                                {
                                    result.add(allLines.get(j));
                                }
                                return result.toString();
                            }
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "Item not found or error occurred";
    }

    /**
     * Adds the specified quantity to an existing item.
     * Prints a message if the item is not found.
     */
    
    public static void addItem(String itemName, int quantityToAdd)
    {
        try
        {
            List<String> lines = readAllLines();
            boolean found = false;

            for (int i = 0; i < lines.size(); i++)
            {
                if (lines.get(i).trim().equals("{") && i + 4 < lines.size())
                {
                    String nameLine = lines.get(i + 1).trim();
                    String name = extractValue(nameLine, "Item=");
                    if (name.equalsIgnoreCase(itemName))
                    {
                        String qtyLine = lines.get(i + 3).trim();
                        int currentQty = Integer.parseInt(extractValue(qtyLine, "Quantity="));
                        int newQty = currentQty + quantityToAdd;
                        lines.set(i + 3, "    Quantity=" + newQty + ";");
                        found = true;
                        break;
                    }
                }
            }

            if (!found)
            {
                System.out.println("Item not found. Use makeItem() to add new items.");
                return;
            }

            writeAllLines(lines);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new item entry with given name, price, and quantity,
     * appending it to the data file.
     */
    
    public static void makeItem(String itemName, double price, int quantity)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true)))
        {
            writer.write("{");
            writer.newLine();
            writer.write("    Item=" + itemName + ";");
            writer.newLine();
            writer.write("    Price=" + price + ";");
            writer.newLine();
            writer.write("    Quantity=" + quantity + ";");
            writer.newLine();
            writer.write("}");
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Removes the specified quantity from an existing item.
     * If the removal would cause negative quantity, operation is aborted.
     * Prints message if item is not found.
     */
    
    public static void removeItem(String itemName, int quantityToRemove)
    {
        try
        {
            List<String> lines = readAllLines();
            boolean found = false;

            for (int i = 0; i < lines.size(); i++)
            {
                if (lines.get(i).trim().equals("{"))
                {
                    int itemLineIndex = -1;
                    int quantityLineIndex = -1;

                    for (int j = i + 1; j < lines.size() && !lines.get(j).trim().equals("}"); j++)
                    {
                        String currentLine = lines.get(j).trim();

                        if (currentLine.startsWith("Item="))
                        {
                            String itemValue = extractValue(currentLine, "Item=");
                            if (itemValue.equalsIgnoreCase(itemName))
                            {
                                itemLineIndex = j;
                            }
                        }
                        else if (currentLine.startsWith("Quantity="))
                        {
                            quantityLineIndex = j;
                        }
                    }

                    if (itemLineIndex != -1 && quantityLineIndex != -1)
                    {
                        int currentQty = Integer.parseInt(extractValue(lines.get(quantityLineIndex).trim(), "Quantity="));
                        int newQty = currentQty - quantityToRemove;

                        if (newQty < 0)
                        {
                            System.out.println("Quantity of item '" + itemName + "' would go negative. Operation aborted.");
                            return;
                        }

                        lines.set(quantityLineIndex, "    Quantity=" + newQty + ";");
                        found = true;
                        break;
                    }
                }
            }

            if (!found)
            {
                System.out.println("Item '" + itemName + "' not found.");
                return;
            }

            writeAllLines(lines);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Removes specified quantities of multiple items at once.
     * Aborts entire operation if any item quantity would go negative.
     * Prints messages for items not found.
     */
    
    public static void removeMultipleItems(Map<String, Integer> itemsToRemove)
    {
        try
        {
            List<String> lines = readAllLines();
            Map<String, Integer> foundItems = new HashMap<>();

            for (int i = 0; i < lines.size(); i++)
            {
                if (lines.get(i).trim().equals("{"))
                {
                    int quantityLineIndex = -1;
                    String currentItemName = null;

                    for (int j = i + 1; j < lines.size() && !lines.get(j).trim().equals("}"); j++)
                    {
                        String currentLine = lines.get(j).trim();

                        if (currentLine.startsWith("Item="))
                        {
                            currentItemName = extractValue(currentLine, "Item=");
                        }
                        else if (currentLine.startsWith("Quantity="))
                        {
                            quantityLineIndex = j;
                        }
                    }

                    if (currentItemName != null && itemsToRemove.containsKey(currentItemName) && quantityLineIndex != -1)
                    {
                        int currentQty = Integer.parseInt(extractValue(lines.get(quantityLineIndex).trim(), "Quantity="));
                        int qtyToRemove = itemsToRemove.get(currentItemName);
                        int newQty = currentQty - qtyToRemove;

                        if (newQty < 0)
                        {
                            System.out.println("Quantity of item '" + currentItemName + "' would go negative. Operation aborted.");
                            return;
                        }

                        lines.set(quantityLineIndex, "    Quantity=" + newQty + ";");
                        foundItems.put(currentItemName, newQty);
                    }
                }
            }

            for (String item : itemsToRemove.keySet())
            {
                if (!foundItems.containsKey(item))
                {
                    System.out.println("Item '" + item + "' not found.");
                }
            }

            writeAllLines(lines);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static List<String> readAllLines() throws IOException
    {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }
        }
        return lines;
    }

    private static void writeAllLines(List<String> lines) throws IOException
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {
            for (String line : lines)
            {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static String extractValue(String line, String key)
    {
        return line.substring(key.length(), line.length() - 1).trim();
    }

    /**
     * Returns the entire content of the data file as a single String.
     */
    
    public static String fileToString()
    {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Error reading file.";
        }
        return sb.toString();
    }
    
    public static void setFilePath(String fp)
    {
    	filePath=fp;
    }
}
