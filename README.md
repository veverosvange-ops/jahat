# COUNTERFLAME binary reconstruction

This repository template analyzes `COUNTERFLAME.dylib` with Ghidra and exports
readable decompiler pseudocode. It does **not** recover the exact original source
project: comments, original variable names, project layout, and some type
information are generally lost during compilation.

## Repository layout

```text
COUNTERFLAME/
├── .github/
│   └── workflows/
│       └── reconstruct.yml
├── scripts/
│   └── ExportFunctions.java
├── COUNTERFLAME.dylib
└── README.md
```

The workflow creates its `output/` directory during the run.

## Run it

1. Create a GitHub repository.
2. Upload `COUNTERFLAME.dylib` to the repository root.
3. Add `.github/workflows/reconstruct.yml`.
4. Add `scripts/ExportFunctions.java`.
5. Open **Actions → Reconstruct COUNTERFLAME → Run workflow**.
6. When the run finishes, download the `COUNTERFLAME-RECONSTRUCTED-SOURCE` artifact.

## Output

- `all-functions.c`: combined decompiler pseudocode.
- `functions/*.c`: one pseudocode file per discovered function.
- `function-list.txt`: function names and addresses.
- `strings.txt`: printable strings found in the binary.
- `symbols.txt`: symbols, when available.
- `assembly.txt`: disassembly, when supported by the installed LLVM tools.
- `binary-info.txt`: file identification.
- `decompile-summary.txt`: counts and architecture information.

## Notes

- A `.dylib` is a compiled Mach-O library, not a source archive.
- Ghidra output is reconstructed pseudocode, not guaranteed compilable C.
- Objective-C/Swift metadata may help identify classes and methods, but it does
  not guarantee reconstruction of original `.m`, `.h`, or `.swift` files.
- Only analyze binaries you own or are authorized to inspect.
