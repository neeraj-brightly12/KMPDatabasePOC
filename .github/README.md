# GitHub Automation Agents

This directory contains GitHub Actions workflows (agents) that automate various tasks for the KMP Database POC project.

## Workflows

### 1. Build Check (`build-check.yml`)
**Triggers:** Push/PR to main or develop branches

**Tasks:**
- Builds Android and iOS targets
- Runs all tests
- Generates APK and iOS framework
- Uploads build artifacts

**Artifacts:** Build reports, test results, APK files

---

### 2. Code Quality Check (`code-quality.yml`)
**Triggers:** Push/PR to main or develop branches

**Tasks:**
- Runs ktlint for code formatting
- Runs detekt for static analysis
- Checks dependency updates
- Generates dependency tree

**Artifacts:** Linting reports, code analysis reports

---

### 3. PR Review Bot (`pr-review.yml`)
**Triggers:** PR opened/updated

**Tasks:**
- Validates PR title follows conventional commits
- Checks branch naming convention
- Analyzes PR size and adds labels
- Performs automated code review
  - Detects TODO comments
  - Finds debug print statements
  - Suggests string resource usage
- Runs test coverage

**Comments:** Adds automated review comments to PR

---

### 4. Auto Label (`auto-label.yml`)
**Triggers:** PR/Issue opened

**Tasks:**
- Labels PRs based on files changed:
  - `ui` - UI files
  - `database` - Database files
  - `viewmodel` - ViewModel files
  - `repository` - Repository files
  - `tests` - Test files
  - `documentation` - MD files
  - `build` - Gradle files
  - `android` - Android specific
  - `ios` - iOS specific
- Labels issues based on content:
  - `bug` - Bug reports
  - `enhancement` - Feature requests
  - `documentation` - Docs
  - `question` - Questions

---

### 5. Release Build (`release.yml`)
**Triggers:** Tag push (v*.*.*) or manual dispatch

**Tasks:**
- Builds release APK and AAB
- Builds iOS framework
- Signs Android artifacts (when configured)
- Creates GitHub release
- Uploads release artifacts

**Manual Trigger:** Can be triggered manually with custom version

---

### 6. Stale Issue Management (`stale-check.yml`)
**Triggers:** Daily at midnight or manual

**Tasks:**
- Marks issues/PRs as stale after 30 days
- Closes stale items after 7 more days
- Exempts pinned and security items

---

## Templates

### Pull Request Template
Located at: `PULL_REQUEST_TEMPLATE.md`

**Sections:**
- Description
- Type of change
- Related issues
- Changes made
- Screenshots
- Testing checklist
- Code review checklist

### Issue Templates

#### Bug Report (`ISSUE_TEMPLATE/bug_report.md`)
- Bug description
- Reproduction steps
- Expected vs actual behavior
- Environment details
- Logs/stack trace

#### Feature Request (`ISSUE_TEMPLATE/feature_request.md`)
- Feature description
- Problem statement
- Proposed solution
- Use cases
- Benefits
- Priority level

---

## Dependabot Configuration

Located at: `dependabot.yml`

**Updates:**
- Gradle dependencies (weekly on Mondays)
- GitHub Actions (weekly on Mondays)
- Auto-creates PRs with labels
- Maximum 10 dependency PRs open at once

---

## Branch Naming Convention

The PR review bot enforces these branch name patterns:
- `feature/*` - New features
- `bugfix/*` - Bug fixes
- `hotfix/*` - Critical fixes
- `release/*` - Release branches

**Examples:**
- `feature/add-product-screen`
- `bugfix/fix-database-crash`
- `hotfix/critical-security-fix`
- `release/v1.2.0`

---

## PR Title Convention

The PR review bot enforces conventional commits format:

**Format:** `type(scope): description`

**Types:**
- `feat` - New feature
- `fix` - Bug fix
- `docs` - Documentation
- `style` - Formatting
- `refactor` - Code restructuring
- `perf` - Performance improvement
- `test` - Tests
- `build` - Build system
- `ci` - CI/CD
- `chore` - Maintenance

**Examples:**
- `feat(ui): add product management screen`
- `fix(database): resolve Room migration issue`
- `docs(readme): update installation instructions`
- `refactor(viewmodel): simplify state management`

---

## Setting Up Secrets

For the workflows to work properly, configure these secrets in GitHub Settings > Secrets:

### For Release Builds:
- `SIGNING_KEY` - Base64 encoded signing keystore
- `ALIAS` - Keystore alias
- `KEY_STORE_PASSWORD` - Keystore password
- `KEY_PASSWORD` - Key password

---

## Labels Used

The automation creates and uses these labels:

**Size Labels:**
- `size/XS` - < 50 lines changed
- `size/S` - < 200 lines changed
- `size/M` - < 500 lines changed
- `size/L` - < 1000 lines changed
- `size/XL` - > 1000 lines changed

**Component Labels:**
- `ui`, `database`, `viewmodel`, `repository`
- `tests`, `documentation`, `build`
- `android`, `ios`

**Type Labels:**
- `bug`, `enhancement`, `question`
- `dependencies`, `github-actions`, `automated`
- `stale`, `pinned`, `security`

---

## Manual Triggers

Some workflows can be triggered manually:

1. Go to Actions tab in GitHub
2. Select the workflow
3. Click "Run workflow"
4. Fill in parameters if required

**Workflows with manual trigger:**
- Release Build
- Stale Issue Management

---

## Customization

To customize the workflows:

1. Edit workflow files in `.github/workflows/`
2. Adjust trigger conditions
3. Modify job steps
4. Update labels and messages
5. Configure review thresholds

---

## Monitoring

Check workflow status:
1. Go to the Actions tab in GitHub
2. View workflow runs
3. Check logs for failures
4. Download artifacts

**Artifacts available for:**
- Build reports (7 days)
- Test results (7 days)
- APK files (14 days)
- Release builds (30 days)

---

## Best Practices

1. Keep PRs small (< 500 lines)
2. Follow branch naming convention
3. Use conventional commit format
4. Add tests for new features
5. Update documentation
6. Respond to automated review comments
7. Keep dependencies up to date

---

## Troubleshooting

**Build failures:**
- Check Java version (should be 17)
- Verify Gradle cache
- Review build logs

**Test failures:**
- Check test logs in artifacts
- Run tests locally first
- Review coverage reports

**Workflow not running:**
- Check trigger conditions
- Verify workflow file syntax
- Check branch/tag patterns

---

## Support

For issues with GitHub Actions:
1. Check workflow logs
2. Review GitHub Actions documentation
3. Open an issue with `github-actions` label