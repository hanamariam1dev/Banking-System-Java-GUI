# GitHub Folder Structure - CBE Banking Management System

## Complete Repository Structure

```
CBE-Banking-System/
├── README.md
├── .gitignore
├── LICENSE
├── GITHUB_FOLDER_STRUCTURE.md
├── .vscode/
│   ├── launch.json
│   └── settings.json
├── docs/
│   ├── API_DOCUMENTATION.md
│   ├── DATABASE_SCHEMA.md
│   ├── SETUP_GUIDE.md
│   └── TEAM_GUIDELINES.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── core/
│   │   │   │   ├── Main.java
│   │   │   │   ├── AppFrame.java
│   │   │   │   ├── AppConstants.java
│   │   │   │   └── BankingConfig.java
│   │   │   ├── authentication/
│   │   │   │   ├── LoginRegister.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── PasswordService.java
│   │   │   │   └── SessionManager.java
│   │   │   ├── dashboard/
│   │   │   │   ├── Dashboard.java
│   │   │   │   ├── DashboardSidebar.java
│   │   │   │   ├── DashboardViews.java
│   │   │   │   └── AdminDashboard.java
│   │   │   ├── components/
│   │   │   │   ├── UIComponents.java
│   │   │   │   ├── SidebarItem.java
│   │   │   │   ├── StatCard.java
│   │   │   │   ├── CardPanel.java
│   │   │   │   ├── GradientPanel.java
│   │   │   │   ├── CBELogoPanel.java
│   │   │   │   ├── NotificationBanner.java
│   │   │   │   ├── SidebarSectionLabel.java
│   │   │   │   ├── GreetingGenerator.java
│   │   │   │   └── PrimaryButton.java
│   │   │   ├── forms/
│   │   │   │   ├── RoundedTextField.java
│   │   │   │   ├── RoundedPasswordField.java
│   │   │   │   ├── QuickActionButton.java
│   │   │   │   ├── FormBuilder.java
│   │   │   │   ├── DialogFactory.java
│   │   │   │   ├── TransferType.java
│   │   │   │   ├── ActionCellRenderer.java
│   │   │   │   ├── DownloadButtonEditor.java
│   │   │   │   └── UserActionEditor.java
│   │   │   ├── services/
│   │   │   │   ├── AccountService.java
│   │   │   │   ├── ProfileService.java
│   │   │   │   ├── TransactionService.java
│   │   │   │   ├── BalanceManager.java
│   │   │   │   ├── AirtimeService.java
│   │   │   │   ├── TravelBookingService.java
│   │   │   │   ├── PDFService.java
│   │   │   │   ├── CSVExporter.java
│   │   │   │   └── QRCodeGenerator.java
│   │   │   ├── database/
│   │   │   │   ├── DBConnection.java
│   │   │   │   ├── DBSchema.java
│   │   │   │   ├── DBSeeder.java
│   │   │   │   ├── DBConfig.java
│   │   │   │   └── ConnectionManager.java
│   │   │   ├── utilities/
│   │   │   │   ├── BankingUtils.java
│   │   │   │   ├── InputValidator.java
│   │   │   │   ├── ValidationService.java
│   │   │   │   ├── DateUtils.java
│   │   │   │   ├── CurrencyFormatter.java
│   │   │   │   ├── SearchUtils.java
│   │   │   │   ├── ClipboardUtils.java
│   │   │   │   ├── ImageUtils.java
│   │   │   │   ├── NotificationManager.java
│   │   │   │   ├── FAQManager.java
│   │   │   │   └── LogManager.java
│   │   │   ├── security/
│   │   │   │   ├── SecurityUtils.java
│   │   │   │   └── PasswordService.java
│   │   │   ├── theme/
│   │   │   │   ├── ThemeManager.java
│   │   │   │   ├── ColorTheme.java
│   │   │   │   └── AnimationUtils.java
│   │   │   ├── models/
│   │   │   │   ├── TransactionTableModel.java
│   │   │   │   ├── UserTableModel.java
│   │   │   │   ├── TransactionRow.java
│   │   │   │   └── AdminSummaryCard.java
│   │   │   ├── localization/
│   │   │   │   ├── LanguageManager.java
│   │   │   │   ├── LocaleConfig.java
│   │   │   │   └── GreetingGenerator.java
│   │   │   └── navigation/
│   │   │       ├── NavigationController.java
│   │   │       └── SessionManager.java
│   │   └── resources/
│   │       ├── images/
│   │       │   └── image.png
│   │       ├── database/
│   │       │   └── banking_system1.sql
│   │       └── config/
│   │           └── application.properties
│   ├── test/
│   │   └── java/
│   │       ├── authentication/
│   │       ├── services/
│   │       ├── database/
│   │       └── utilities/
│   └── lib/
│       ├── commons-7.2.5.jar
│       ├── core-3.5.3.jar
│       ├── io-7.2.5.jar
│       ├── javase-3.5.3.jar
│       ├── kernel-7.2.5.jar
│       ├── layout-7.2.5.jar
│       ├── mysql-connector-j-9.6.0.jar
│       ├── slf4j-api-1.7.36.jar
│       ├── slf4j-simple-1.7.36.jar
│       ├── zxing-core-3.5.1.jar
│       └── zxing-javase-3.5.1.jar
├── scripts/
│   ├── build.bat
│   ├── run.bat
│   ├── setup.bat
│   └── database-setup.bat
├── assets/
│   ├── images/
│   │   ├── logo.png
│   │   ├── icons/
│   │   └── screenshots/
│   └── fonts/
├── config/
│   ├── development.properties
│   ├── production.properties
│   └── test.properties
├── database/
│   ├── schema/
│   │   └── banking_system1.sql
│   ├── migrations/
│   └── seeds/
├── tests/
│   ├── unit/
│   ├── integration/
│   └── ui/
├── .github/
│   ├── workflows/
│   │   ├── ci.yml
│   │   ├── build.yml
│   │   └── deploy.yml
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   └── PULL_REQUEST_TEMPLATE.md
└── team/
    ├── mahlet/
    ├── hanamariam/
    ├── wubet/
    ├── ezedin/
    └── mubarek/
```

## Folder Descriptions

### Root Level Files
- **README.md** - Project overview and setup instructions
- **.gitignore** - Git ignore rules
- **LICENSE** - Project license
- **CONTRIBUTING.md** - Contribution guidelines
- **WORKLOAD_DISTRIBUTION.md** - Team workload planning
- **GITHUB_FOLDER_STRUCTURE.md** - This file

### Source Code Structure (`src/`)

#### `src/main/java/` - Main Application Code
- **core/** - Core application classes (Main, AppFrame, BankingConfig)
- **authentication/** - Login, registration, session management
- **dashboard/** - Dashboard components and admin interface
- **components/** - Reusable UI components
- **forms/** - Form-related components and editors
- **services/** - Business logic services
- **database/** - Database connection and management
- **utilities/** - Helper and utility classes
- **security/** - Security-related functionality
- **theme/** - Theming and visual styling
- **models/** - Data models and table models
- **localization/** - Multi-language support
- **navigation/** - Navigation controllers

#### `src/main/resources/` - Application Resources
- **images/** - Application images and icons
- **database/** - Database scripts
- **config/** - Configuration files

#### `src/test/java/` - Test Code
- Organized by package structure matching main code

#### `src/lib/` - External Dependencies
- All JAR files required for the project

### Configuration (`config/`)
- Environment-specific configuration files

### Database (`database/`)
- Schema, migrations, and seed data

### Tests (`tests/`)
- Unit, integration, and UI tests

### GitHub Configuration (`.github/`)
- CI/CD workflows
- Issue and PR templates

### Team Structure (`team/`)
- Individual team member folders for personal development

## File Organization by Team Member

### Mahlet's Files
```
src/main/java/core/
├── Main.java
├── AppFrame.java
├── AppConstants.java
└── BankingConfig.java

src/main/java/dashboard/
├── Dashboard.java
└── AdminDashboard.java

src/main/java/models/
└── AdminSummaryCard.java
```

### Hanamariam's Files
```
src/main/java/authentication/
├── LoginRegister.java
├── AuthController.java
└── PasswordService.java

src/main/java/dashboard/
├── DashboardSidebar.java
└── DashboardViews.java

src/main/java/services/
├── AccountService.java
└── ProfileService.java

src/main/java/localization/
└── LanguageManager.java
```

### Wubet's Files
```
src/main/java/components/
├── UIComponents.java
├── SidebarItem.java
└── StatCard.java

src/main/java/services/
├── TransactionService.java
├── PDFService.java
├── CSVExporter.java
├── QRCodeGenerator.java
├── AirtimeService.java
└── TravelBookingService.java

src/main/java/models/
├── TransactionTableModel.java
├── TransactionRow.java
├── UserTableModel.java
└── UserActionEditor.java
```

### Ezedin's Files
```
src/main/java/utilities/
├── BankingUtils.java
├── InputValidator.java
├── ValidationService.java
├── DateUtils.java
├── CurrencyFormatter.java
├── SearchUtils.java
├── ClipboardUtils.java
├── ImageUtils.java
├── NotificationManager.java
├── FAQManager.java
└── LogManager.java

src/main/java/database/
├── DBConnection.java
├── DBSchema.java
├── DBSeeder.java
├── DBConfig.java
└── ConnectionManager.java

src/main/java/security/
└── SecurityUtils.java

src/main/java/navigation/
├── NavigationController.java
└── SessionManager.java
```

### Mubarek's Files
```
src/main/java/theme/
├── ThemeManager.java
├── ColorTheme.java
└── AnimationUtils.java

src/main/java/components/
├── GradientPanel.java
├── CBELogoPanel.java
├── CardPanel.java
├── NotificationBanner.java
├── SidebarSectionLabel.java
└── PrimaryButton.java

src/main/java/forms/
├── RoundedTextField.java
├── RoundedPasswordField.java
├── QuickActionButton.java
├── FormBuilder.java
├── DialogFactory.java
├── TransferType.java
├── ActionCellRenderer.java
└── DownloadButtonEditor.java

src/main/java/services/
└── BalanceManager.java

src/main/java/localization/
├── LocaleConfig.java
└── GreetingGenerator.java

src/main/java/database/
└── ConnectionManager.java
```

## Git Workflow Structure

### Branch Strategy
```
main                    # Production-ready code
├── dev                # Integration branch
├── feature/mahlet      # Mahlet's feature branch
├── feature/hanamariam # Hanamariam's feature branch
├── feature/wubet      # Wubet's feature branch
├── feature/ezedin     # Ezedin's feature branch
└── feature/mubarek    # Mubarek's feature branch
```

### Commit Convention
```
feat: Add new feature
fix: Fix bug
docs: Update documentation
style: Code formatting
refactor: Code refactoring
test: Add tests
chore: Maintenance tasks
```

## Setup Instructions

1. **Clone Repository**
   ```bash
   git clone <repository-url>
   cd CBE-Banking-System
   ```

2. **Setup Database**
   ```bash
   # Run database setup script
   scripts/database-setup.bat
   ```

3. **Install Dependencies**
   - All JAR files are included in `src/lib/`
   - No additional installation required

4. **Run Application**
   ```bash
   # Use the run script
   scripts/run.bat
   
   # Or compile and run manually
   javac -cp "src/lib/*" src/main/java/core/Main.java
   java -cp "src/lib/*:src/main/java" core.Main
   ```

## Benefits of This Structure

1. **Clear Separation** - Each team member works in their own package
2. **No Conflicts** - File ownership prevents Git merge conflicts
3. **Scalability** - Easy to add new features and team members
4. **Maintainability** - Logical organization makes code easy to find
5. **Testing** - Separate test structure for comprehensive testing
6. **Documentation** - All documentation organized and accessible
7. **CI/CD Ready** - GitHub workflows setup for automation

---

*This structure ensures smooth collaboration, clear ownership, and professional organization for the CBE Banking Management System project.*
