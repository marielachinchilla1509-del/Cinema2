package ui;

import proyecto1.cinema.TicketSalesModule;
import ui.ProductUI.Product;
import proyecto1.cinema.Customer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Map;
import java.util.TreeMap;

/**
 * SellProductUI: GUI for selling products to customers in a cinema context.
 * *
 * Features:
 * - Customer ID validation.
 * - Product search and dynamic filtering.
 * - Display products grouped by category with stock management.
 * - Add products to cart and calculate subtotal, discount, tax, and total.
 * - Generate and display a printable product invoice.
 * - Save invoices to disk.
 */
public class SellProductUI extends JFrame {

	/** Colors for UI styling */
	private final Color red = new Color(139, 0, 0);
	private final Color navyBlue = new Color(10, 25, 60);
	private final Color white = Color.WHITE;
	private final Color darkGray = new Color(0x2E2E2E);

	/** Ticket sales module reference */
	private final TicketSalesModule module;

	/** Total cost of selected products */
	private double total = 0.0;

	/** Label displaying total */
	private JLabel totalLabel;

	/** Products selected for purchase */
	private Vector<Product> productsToSell = new Vector<>();

	/** Available products to sell */
	private final Vector<Product> availableProducts = ProductUI.getProducts();

	/** Folder for saving invoices */
	private final String INVOICE_FOLDER = "invoices";

	/** UI components for customer validation */
	private JTextField txtClientId;
	private JTextField txtClientName;

	/** UI components for product search and display */
	private JTextField txtSearch;
	private JPanel productsContainerPanel;
	private JScrollPane productsScrollPane;

	/** Currently validated customer */
	private Customer foundCustomer = null;

	/** Name of the customer for display */
	private String customerName = "N/A (No Validated)";

	/**
	 * Constructor: Initializes UI and layout.
	 *
	 * @param module Reference to TicketSalesModule
	 */
	public SellProductUI(TicketSalesModule module) {
		this.module = module;

		setTitle("🍿 Sell Product - CINEMA UCR");
		setSize(700, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.WHITE);
		setLayout(new BorderLayout(15, 15));

		// ==========================================================
		// NORTH PANEL: CUSTOMER VALIDATION
		// ==========================================================
		JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		clientPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(navyBlue.darker()),
				"👤 Customer ID Validation (Mandatory)",
				javax.swing.border.TitledBorder.LEFT,
				javax.swing.border.TitledBorder.TOP,
				new Font("Inter", Font.BOLD, 12), navyBlue));
		clientPanel.setBackground(new Color(245, 245, 255));

		txtClientId = new JTextField(10);
		txtClientId.setToolTipText("Enter Customer ID");

		txtClientName = new JTextField("N/A", 20);
		txtClientName.setEditable(false);
		txtClientName.setBackground(Color.LIGHT_GRAY);

		JButton btnValidate = new JButton("Validate ID");
		btnValidate.setBackground(navyBlue);
		btnValidate.setForeground(white);
		btnValidate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		clientPanel.add(new JLabel("ID:"));
		clientPanel.add(txtClientId);
		clientPanel.add(btnValidate);
		clientPanel.add(new JLabel("Client:"));
		clientPanel.add(txtClientName);

		btnValidate.addActionListener(e -> validateClient());
		txtClientId.addActionListener(e -> validateClient());

		add(clientPanel, BorderLayout.NORTH);

		// ==========================================================
		// CENTER PANEL: PRODUCT SEARCH AND DISPLAY
		// ==========================================================
		productsContainerPanel = new JPanel(new BorderLayout(10, 10));
		productsContainerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
		productsContainerPanel.setBackground(white);

		// Search bar
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		searchPanel.setBackground(white);
		JLabel searchLabel = new JLabel("🔍 Search Product:");
		searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtSearch = new JTextField(30);
		txtSearch.setToolTipText("Enter product name or code to filter");

		searchPanel.add(searchLabel);
		searchPanel.add(txtSearch);

		productsContainerPanel.add(searchPanel, BorderLayout.NORTH);

		// Search listener updates product display dynamically
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) { updateProductDisplay(); }
			@Override
			public void removeUpdate(DocumentEvent e) { updateProductDisplay(); }
			@Override
			public void changedUpdate(DocumentEvent e) { updateProductDisplay(); }
		});

		// Scroll pane for product listing
		productsScrollPane = new JScrollPane();
		productsScrollPane.setBorder(null);
		productsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		productsContainerPanel.add(productsScrollPane, BorderLayout.CENTER);

		// Display initial products
		updateProductDisplay();
		add(productsContainerPanel, BorderLayout.CENTER);

		// ==========================================================
		// SOUTH PANEL: TOTAL AND CONTROLS
		// ==========================================================
		JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
		bottomPanel.setBackground(white);

		totalLabel = new JLabel("Total (with Tax): $0.00", SwingConstants.CENTER);
		totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		totalLabel.setForeground(red);

		JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		controlPanel.setBackground(white);

		JButton btnFinish = new JButton("✅ Finish Purchase");
		btnFinish.setBackground(navyBlue.darker());
		btnFinish.setForeground(white);
		btnFinish.setFont(new Font("Segoe UI", Font.BOLD, 18));
		btnFinish.setFocusPainted(false);
		btnFinish.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnFinish.addActionListener(this::finishPurchase);

		JButton btnCancel = new JButton("❌ Cancel");
		btnCancel.setBackground(darkGray);
		btnCancel.setForeground(white);
		btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		btnCancel.setFocusPainted(false);
		btnCancel.addActionListener(e -> dispose());

		controlPanel.add(btnFinish);
		controlPanel.add(btnCancel);

		bottomPanel.add(totalLabel, BorderLayout.NORTH);
		bottomPanel.add(controlPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		setVisible(true);
	}
    
    // ==========================================================
	// NEW: PRODUCT PERSISTENCE LOGIC (GUARDA EL STOCK EN ARCHIVO)
	// ==========================================================
	
	/**
	 * Guarda el estado actual de los productos disponibles (incluyendo el stock actualizado)
	 * de vuelta al archivo de datos de productos ("products.txt").
	 * * NOTA: Esta implementación asume que el archivo de productos es 'products.txt'
	 * y utiliza un formato simple CSV (Code,Name,Price,Stock,Category,Status) para sobrescribirlo.
	 */
	private void saveProductsToFile() {
		String PRODUCTS_FILE = "products.txt";
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRODUCTS_FILE, false))) { 
			// Usar 'false' en FileWriter sobrescribe el contenido, lo cual es necesario para actualizar el stock.
			
			// Formato asumido: Code,Name,Price,Stock,Category,Status
			for (Product p : availableProducts) {
				// Utilizamos métodos que deben existir en la clase Product
				String line = String.format("%s,%s,%.2f,%d,%s,%s",
						p.getCode(), p.getName(), p.getPrice(), p.getStock(), p.getCategory(), p.getStatus());
				bw.write(line);
				bw.newLine();
			}
			
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this,
					"❌ Error saving product stock persistence: " + ex.getMessage(),
					"Stock Save Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	// ==========================================================
	// UPDATE PRODUCT DISPLAY DYNAMICALLY
	// ==========================================================
	/**
	 * Updates the displayed products grouped by category and filtered
	 * according to the search field.
	 */
	private void updateProductDisplay() {
		String searchText = txtSearch.getText().trim().toLowerCase();

		// Categorize active products that match search
		Map<String, Vector<Product>> categorizedProducts = new TreeMap<>();
		for (Product p : availableProducts) {
			// MODIFICACIÓN: Solo mostrar si el producto está Activo Y el stock es mayor a 0
			if ("Active".equalsIgnoreCase(p.getStatus()) && p.getStock() > 0) {
				boolean matchesSearch = searchText.isEmpty() ||
						p.getName().toLowerCase().contains(searchText) ||
						p.getCode().toLowerCase().contains(searchText);
				if (matchesSearch) {
					String category = p.getCategory();
					categorizedProducts.computeIfAbsent(category, k -> new Vector<>()).add(p);
				}
			}
		}

		// Panel for all categories
		JPanel mainDisplayPanel = new JPanel();
		mainDisplayPanel.setLayout(new BoxLayout(mainDisplayPanel, BoxLayout.Y_AXIS));
		mainDisplayPanel.setBackground(white);

		boolean foundAnyProduct = false;

		// Create category panels
		for (Map.Entry<String, Vector<Product>> entry : categorizedProducts.entrySet()) {
			String category = entry.getKey();
			Vector<Product> products = entry.getValue();

			if (!products.isEmpty()) {
				JLabel categoryHeader = new JLabel("--- " + category.toUpperCase() + " ---");
				categoryHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
				categoryHeader.setForeground(navyBlue);
				categoryHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

				JPanel headerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
				headerWrapper.setBackground(white);
				headerWrapper.add(categoryHeader);
				mainDisplayPanel.add(headerWrapper);

				// Grid for category products
				int cols = 3;
				JPanel categoryPanel = new JPanel(new GridLayout(0, cols, 20, 20));
				categoryPanel.setBackground(white);
				categoryPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

				for (Product p : products) {
					categoryPanel.add(createProductCard(p));
					foundAnyProduct = true;
				}
				mainDisplayPanel.add(categoryPanel);
			}
		}

		if (!foundAnyProduct) {
			JLabel noProductsLabel = new JLabel("No active products found or matching search.", SwingConstants.CENTER);
			noProductsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
			noProductsLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
			mainDisplayPanel.add(noProductsLabel);
		}

		productsScrollPane.setViewportView(mainDisplayPanel);
		productsScrollPane.revalidate();
		productsScrollPane.repaint();
	}

	/**
	 * Creates a visual card panel for a product.
	 * @param p Product to display
	 * @return JPanel representing the product
	 */
	private JPanel createProductCard(Product p) {
		JPanel card = new JPanel(new BorderLayout(5, 5));
		card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		card.setBackground(new Color(250, 250, 250));

		// Panel para Nombre y Stock (alineación vertical)
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBackground(new Color(250, 250, 250));

		JLabel nameLabel = new JLabel(p.getName(), SwingConstants.CENTER);
		nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Etiqueta de Stock (A pesar de que el producto se oculta si stock es 0,
		// esta tarjeta solo se crea si el stock es > 0, pero se mantiene la lógica de color).
		String stockText = String.format("Stock: %d", p.getStock());
		JLabel stockLabel = new JLabel(stockText, SwingConstants.CENTER);
		stockLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10)); // Letras pequeñas
		// Colores para indicar el estado del stock
		stockLabel.setForeground(p.getStock() > 5 ? Color.GRAY.darker() : new Color(200, 100, 0));
		stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		headerPanel.add(nameLabel);
		headerPanel.add(stockLabel);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		card.add(headerPanel, BorderLayout.NORTH);

		JLabel priceLabel = new JLabel(String.format("Price: $%.2f", p.getPrice()), SwingConstants.CENTER);
		priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		card.add(priceLabel, BorderLayout.CENTER);

		JButton addButton = new JButton("Add to Cart");
		addButton.setBackground(navyBlue);
		addButton.setForeground(white);
		addButton.setFocusPainted(false);
		addButton.addActionListener(e -> addProductToSell(p));
		card.add(addButton, BorderLayout.SOUTH);

		return card;
	}

	/**
	 * Adds a product to the current sale, updates total and stock.
	 * @param p Product to add
	 */
	private void addProductToSell(Product p) {
		if (p.getStock() > 0) {
			productsToSell.add(p);
			// Descuenta el stock. Esto dispara updateProductDisplay() y oculta el producto si llega a 0.
			p.setStock(p.getStock() - 1); 
			this.total += p.getPrice();
			updateTotalLabel();
			updateProductDisplay(); // Esto recarga la vista y aplica el nuevo filtro
			JOptionPane.showMessageDialog(this, 
					"Added: " + p.getName() + " to cart.", 
					"Product Added", JOptionPane.INFORMATION_MESSAGE);
		} else {
			// Este mensaje solo sirve como respaldo, ya que el botón debería haber desaparecido.
			JOptionPane.showMessageDialog(this, "Stock is 0 for: " + p.getName());
			updateProductDisplay();
		}
	}

	// ==========================================================
	// CUSTOMER LOGIC
	// ==========================================================
	/**
	 * Searches for a customer by ID in the "customers.txt" file.
	 * @param id Customer ID
	 * @return Customer object if found, null otherwise
	 */
	private Customer searchCustomerFromFile(String id) {
		File file = new File("customers.txt");
		if (!file.exists()) {
			return null;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			String name = null, email = null, phone = null;
			boolean vip = false;

			while ((line = br.readLine()) != null) {
				if (line.startsWith("ID: ") && line.substring(4).trim().equals(id)) {
					line = br.readLine();
					if (line != null && line.startsWith("Name: ")) { name = line.substring(6).trim(); }
					line = br.readLine();
					if (line != null && line.startsWith("Email: ")) { email = line.substring(7).trim(); }
					line = br.readLine();
					if (line != null && line.startsWith("Phone: ")) { phone = line.substring(7).trim(); }
					line = br.readLine();
					if (line != null && line.startsWith("VIP: ")) { vip = Boolean.parseBoolean(line.substring(5).trim()); }

					// Skip blank lines between customers
					br.readLine(); br.readLine();

					return new Customer(id, name, email, phone, vip);
				}
			}
		} catch (Exception ignored) {}
		return null;
	}

	/**
	 * Validates the customer ID entered in the text field and updates UI.
	 */
	private void validateClient() {
		String id = txtClientId.getText().trim();

		if (id.isEmpty()) {
			txtClientName.setText("N/A");
			JOptionPane.showMessageDialog(this, "Enter a valid ID.");
			return;
		}

		Customer c = searchCustomerFromFile(id);

		if (c == null) {
			foundCustomer = null;
			customerName = "N/A (No Validated)";
			txtClientName.setText("Not Found");
			JOptionPane.showMessageDialog(this, "❌ Customer not found.");
		} else {
			foundCustomer = c;
			customerName = c.getName();
			txtClientName.setText(customerName);
			String level = getMembershipLevel();
			double discount = getDiscountPercentage() * 100;
			
			JOptionPane.showMessageDialog(this,
					String.format("✅ Customer Found: %s\n🏷 Membership: %s (%.0f%% Discount)",
						customerName, level, discount));
			updateTotalLabel();
		}
	}

	// ==========================================================
	// LÓGICA DE DESCUENTO Y MEMBRESÍA
	// ==========================================================
	
	/** * Returns discount percentage based on customer membership. */
	private double getDiscountPercentage() {
		if (foundCustomer == null) return 0.0;
		// Si es VIP, 15% de descuento. Si no es VIP (Standard), 0% de descuento.
		return foundCustomer.isVip() ? 0.15 : 0.0; 
	}

	/** * Returns membership level string. */
	private String getMembershipLevel() {
		if (foundCustomer == null) return "N/A";
		// Si es VIP, es Premium. Si no es VIP, es Standard (Sin Descuento).
		return foundCustomer.isVip() ? "Premium" : "Standard (0% Discount)";
	}

	/** Updates total label with tax and discount applied */
	private void updateTotalLabel() {
		double subtotalBase = this.total;	
		double discountAmount = getDiscountAmount(subtotalBase);
		double subtotalAfterDiscount = subtotalBase - discountAmount;
		double tax = subtotalAfterDiscount * 0.13;
		double totalFinal = subtotalAfterDiscount + tax;
		totalLabel.setText(String.format("Total (with Tax): $%.2f", totalFinal));
	}

	/** Calculates discount amount from subtotal */
	private double getDiscountAmount(double subtotal) {
		return subtotal * getDiscountPercentage();
	}


	// ==========================================================
	// FINISH PURCHASE
	// ==========================================================
	/**
	 * Completes the purchase, validates customer and products,
	 * calculates totals, shows invoice, and saves it.
	 */
	private void finishPurchase(ActionEvent e) {
		if (foundCustomer == null) {
			JOptionPane.showMessageDialog(this,	
					"❌ You must validate a customer before selling products.",	
					"Customer Required", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (productsToSell.isEmpty()) {
			JOptionPane.showMessageDialog(this,	
					"❌ Add at least one product to the purchase.",	
					"No Products", JOptionPane.ERROR_MESSAGE);
			return;
		}

		double subtotalBase = this.total;	
		double discountRate = getDiscountPercentage();
		double discountAmount = subtotalBase * discountRate;
		double subtotalAfterDiscount = subtotalBase - discountAmount;
		double tax = subtotalAfterDiscount * 0.13;
		double totalFinal = subtotalAfterDiscount + tax;

		// Record the sale (example placeholder)
		// module.recordProductSale(foundCustomer, productsToSell, totalFinal, "Cash");
        
        // CORRECCIÓN CLAVE: Guarda el stock actualizado antes de generar la factura
        // para que sea persistente la reducción.
        saveProductsToFile();

		showProductInvoice(subtotalBase, discountAmount, tax, totalFinal);

		productsToSell.clear();
		this.total = 0.0;
		updateTotalLabel();
		dispose();
	}

	// ==========================================================
	// INVOICE DISPLAY, PRINT, AND SAVE
	// ==========================================================
	private void showProductInvoice(double subtotalBase, double discountAmount, double tax,
			double totalFinal) {
		JFrame invoiceFrame = new JFrame("🧾 Product Invoice");
		invoiceFrame.setSize(400, 550);
		invoiceFrame.setLocationRelativeTo(null);
		invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String membership = getMembershipLevel();
		double discountRate = getDiscountPercentage() * 100;

		StringBuilder productsDetails = new StringBuilder();
		for (Product p : productsToSell) {
			productsDetails.append(String.format("	 - %s (x1) @ $%.2f\n", p.getName(), p.getPrice()));
		}

		String invoiceContent
				= "----------------------------------------\n"
				+ "	 	 🍿 CINEMA UCR - Product Sales\n"
				+ "----------------------------------------\n"
				+ "Client: " + customerName + "\n"
				+ "Client ID: " + foundCustomer.getId() + "\n"
				+ "Membership: " + membership + String.format(" (%.0f%%)\n", discountRate)
				+ "Date: " + sdf.format(new Date()) + "\n"
				+ "----------------------------------------\n"
				+ "PRODUCTS SOLD:\n"
				+ productsDetails.toString()
				+ "----------------------------------------\n"
				+ String.format("SUBTOTAL: $%.2f\n", subtotalBase)
				+ String.format("DISCOUNT: -$%.2f\n", discountAmount)
				+ "----------------------------------------\n"
				+ String.format("SUBTOTAL NETO: $%.2f\n", (subtotalBase - discountAmount))
				+ String.format("TAX (13%%): $%.2f\n", tax)
				+ "----------------------------------------\n"
				+ String.format("TOTAL: $%.2f\n", totalFinal)
				+ "----------------------------------------\n"
				+ "Thank you for your purchase! 🎬";

		JTextArea txt = new JTextArea(invoiceContent);
		txt.setEditable(false);
		txt.setFont(new Font("Consolas", Font.PLAIN, 16));

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(new JScrollPane(txt), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		JButton btnPrint = new JButton("🖨 Print Invoice");
		btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnPrint.setBackground(navyBlue);
		btnPrint.setForeground(Color.WHITE);
		btnPrint.addActionListener(e -> printInvoice(invoiceContent));

		JButton btnClose = new JButton("❌ Close");
		btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnClose.setBackground(red);
		btnClose.setForeground(Color.WHITE);
		btnClose.addActionListener(e -> invoiceFrame.dispose());

		buttonPanel.add(btnPrint);
		buttonPanel.add(btnClose);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		invoiceFrame.add(contentPanel);
		invoiceFrame.setVisible(true);

		saveInvoiceToFile(invoiceContent);
	}

	private void printInvoice(String content) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(new StringPrintable(content));

		boolean doPrint = job.printDialog();
		if (doPrint) {
			try {
				job.print();
				JOptionPane.showMessageDialog(this, "✅ Invoice sent to printer.");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "❌ Error during printing: "
						+ ex.getMessage(), "Printing Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void saveInvoiceToFile(String content) {
		try {
			File folder = new File(INVOICE_FOLDER);
			if (!folder.exists()) folder.mkdirs();

			File file = new File(folder, "invoice_PRODUCT_" + System.currentTimeMillis() + ".txt");
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.println(content);
			pw.close();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "❌ Error saving file: " + ex.getMessage());
		}
	}

	/** Helper class for printing invoice content */
	private static class StringPrintable implements Printable {
		private final String content;
		private final Font printFont = new Font("Monospaced", Font.PLAIN, 10);
		private final int LINE_HEIGHT = 12;

		public StringPrintable(String content) { this.content = content; }

		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
			if (pageIndex > 0) return NO_SUCH_PAGE;

			Graphics2D g2d = (Graphics2D) graphics;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			g2d.setFont(printFont);

			String[] lines = content.split("\n");
			int y = LINE_HEIGHT;
			for (String line : lines) {
				g2d.drawString(line, 0, y);
				y += LINE_HEIGHT;
			}
			return PAGE_EXISTS;
		}
	}
}