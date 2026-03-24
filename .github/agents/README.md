# AI Agents for Code Analysis

This directory contains specialized AI agent prompts for automated code analysis, review, and improvement tasks. Each agent is designed to focus on a specific aspect of code quality.

## Available Agents

### 🔧 code-optimizer.md
**Purpose**: Analyzes codebase for optimization opportunities
**Use When**:
- Preparing for production release
- Performance optimization sprint
- Refactoring large codebases

**Output**: Detailed optimization suggestions with before/after examples

---

### 👁️ code-reviewer.md
**Purpose**: Comprehensive code review focusing on correctness and best practices
**Use When**:
- Before merging PRs
- Code quality audits
- Onboarding review

**Output**: Categorized issues with severity ratings and recommendations

---

### 🧪 test-generator.md
**Purpose**: Generates comprehensive test suites for untested code
**Use When**:
- Increasing test coverage
- Adding tests to legacy code
- TDD workflow

**Output**: Complete test classes with unit, integration, and UI tests

---

### 📝 documentation-writer.md
**Purpose**: Creates KDoc, README, and architecture documentation
**Use When**:
- Documenting new features
- Creating API documentation
- Onboarding materials needed

**Output**: KDoc comments, README files, architecture docs

---

### 🐛 bug-hunter.md
**Purpose**: Identifies potential bugs, edge cases, and correctness issues
**Use When**:
- Before major releases
- After significant refactoring
- Bug prevention

**Output**: Prioritized bug reports with reproduction scenarios

---

### ⚡ performance-analyzer.md
**Purpose**: Analyzes performance bottlenecks and optimization opportunities
**Use When**:
- App feels sluggish
- High memory usage
- Battery drain issues

**Output**: Performance issues with measurable improvement estimates

---

### 🔒 security-auditor.md
**Purpose**: Security audit for vulnerabilities and data exposure risks
**Use When**:
- Before production release
- Handling sensitive data
- Compliance requirements

**Output**: Security vulnerability report with OWASP classification

---

## How to Use These Agents

### ⚡ Quick Start (Android Studio Terminal + Claude)

**Simplest Method:**
```bash
# In Android Studio Terminal
cat .github/agents/code-reviewer.md

# Copy the output, paste in Claude, then ask:
# "Review ProductViewModel.kt"
```

**See [AGENT_CHEATSHEET.md](../.github/AGENT_CHEATSHEET.md) for quick reference!**

---

### With Claude Code CLI

1. **Select the agent prompt** you want to use
2. **Copy the content** from the agent file
3. **Prepend it to your request** to Claude:

```bash
# Example: Using the code-optimizer agent
claude "$(cat .github/agents/code-optimizer.md)

Please analyze the ProductViewModel.kt file and suggest optimizations."
```

**Or simpler two-step approach:**
```bash
# Step 1: Show agent
cat .github/agents/code-optimizer.md

# Step 2: Copy output, then ask Claude
# "Analyze ProductViewModel.kt and suggest optimizations"
```

### With GitHub Copilot / Other AI Tools

1. Open the agent file (e.g., `code-reviewer.md`)
2. Copy the entire content
3. Paste it as a system prompt or initial context
4. Follow up with your specific request

### As GitHub Issue Templates

You can reference these agents in issue descriptions:

```markdown
**Analysis Requested**: Security Audit

Please review the authentication flow using the `.github/agents/security-auditor.md` guidelines.

Files to review:
- `AuthViewModel.kt`
- `UserRepository.kt`
- `LoginScreen.kt`
```

### In Pull Request Reviews

Reference agents in PR review comments:

```markdown
Please run the `code-optimizer` agent on the new ProductScreen implementation
to identify any performance issues before merging.
```

---

## Agent Workflow Examples

### Example 1: Pre-Release Quality Check

```bash
# 1. Security audit
claude "$(cat .github/agents/security-auditor.md)
Audit the entire src/commonMain/kotlin directory"

# 2. Bug hunting
claude "$(cat .github/agents/bug-hunter.md)
Scan all ViewModels and Repositories for potential bugs"

# 3. Performance check
claude "$(cat .github/agents/performance-analyzer.md)
Analyze all Compose screens for performance issues"

# 4. Documentation review
claude "$(cat .github/agents/documentation-writer.md)
Check all public APIs for missing documentation"
```

### Example 2: New Feature Development

```bash
# 1. Code review after implementation
claude "$(cat .github/agents/code-reviewer.md)
Review the new payment feature in src/payment/"

# 2. Generate tests
claude "$(cat .github/agents/test-generator.md)
Create tests for PaymentViewModel and PaymentRepository"

# 3. Document the feature
claude "$(cat .github/agents/documentation-writer.md)
Create documentation for the payment feature"
```

### Example 3: Legacy Code Improvement

```bash
# 1. Identify issues
claude "$(cat .github/agents/bug-hunter.md)
Analyze src/legacy/ for potential bugs"

# 2. Optimize
claude "$(cat .github/agents/code-optimizer.md)
Suggest refactoring for src/legacy/OldUserManager.kt"

# 3. Add tests
claude "$(cat .github/agents/test-generator.md)
Generate test suite for legacy code"
```

---

## Customizing Agents

You can customize any agent for your specific needs:

1. **Copy the agent file** to create a variant
2. **Modify the focus areas** or checklist
3. **Add project-specific rules**
4. **Adjust severity thresholds**

Example customization:

```markdown
---
name: custom-reviewer
description: Project-specific code reviewer with company standards
---

[Standard agent prompt]

## Additional Project Rules

- All database operations must use the BaseRepository
- ViewModels must use sealed classes for state
- UI components must be in the ui/components/ directory
- All strings must use string resources
```

---

## Agent Output Standards

All agents follow consistent output formats:

### Severity Levels
- **Critical**: Fix immediately (crashes, security, data loss)
- **High**: Fix before release (major bugs, performance issues)
- **Medium**: Plan to fix (minor issues, improvements)
- **Low**: Nice to have (optimizations, style)

### Issue Format
```
[Icon] Issue Title

**Severity**: [Level]
**Category**: [Type]
**Location**: [File:Line]

**Current Code**: [Snippet]
**Problem**: [Description]
**Fix**: [Solution]
**Impact**: [Benefits]
```

### Report Structure
1. **Executive Summary** - High-level overview
2. **Detailed Findings** - Individual issues
3. **Recommendations** - Action items
4. **Metrics** - Counts, priorities, estimates

---

## Integration with CI/CD

You can integrate these agents into your GitHub Actions workflows:

```yaml
# .github/workflows/ai-review.yml
name: AI Code Review

on: pull_request

jobs:
  ai-review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Run Security Audit
        run: |
          # Use AI API to run security-auditor agent
          # Post results as PR comment
```

---

## Best Practices

### DO:
✅ Run agents on focused code sections
✅ Combine multiple agents for comprehensive analysis
✅ Use agents as part of PR review process
✅ Customize agents for your project needs
✅ Review agent suggestions critically

### DON'T:
❌ Blindly apply all suggestions
❌ Skip manual code review
❌ Use agents on generated/vendor code
❌ Ignore agent warnings without reason
❌ Run all agents on every tiny change

---

## Agent Priority Matrix

| Situation | Recommended Agents | Priority |
|-----------|-------------------|----------|
| Pre-release | security-auditor, bug-hunter, performance-analyzer | High |
| New feature | code-reviewer, test-generator, documentation-writer | High |
| Refactoring | code-optimizer, bug-hunter | Medium |
| Bug fix | bug-hunter, test-generator | High |
| Performance issue | performance-analyzer, code-optimizer | High |
| Legacy code | All agents | Medium |

---

## Contributing New Agents

To add a new agent:

1. Create a new `.md` file in `.github/agents/`
2. Follow the standard format:
   ```markdown
   ---
   name: agent-name
   description: Brief description
   ---

   [Detailed prompt]
   ```
3. Document in this README
4. Test with sample code
5. Submit PR

---

## Support

For issues or suggestions about these agents:
1. Open an issue with label `agents`
2. Reference the specific agent file
3. Provide example output/behavior

---

## Version History

- **v1.0.0** (2024): Initial agent collection
  - code-optimizer
  - code-reviewer
  - test-generator
  - documentation-writer
  - bug-hunter
  - performance-analyzer
  - security-auditor