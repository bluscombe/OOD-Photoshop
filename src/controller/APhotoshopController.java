package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import commands.terminal.BrightenCommand;
import commands.terminal.ComponentCommand;
import commands.terminal.FlipCommand;
import commands.terminal.LoadCommand;
import commands.terminal.PhotoshopCommand;
import commands.terminal.SaveCommand;
import model.enums.ComponentGreyscale;
import model.enums.Direction;
import model.PhotoshopModel;
import view.PhotoshopView;

/**
 * PhotoshopControllerImpl is an implementation of PhotoshopController that uses
 * command design pattern.
 */
public abstract class APhotoshopController implements PhotoshopController {
  PhotoshopModel model;
  PhotoshopView view;
  Readable rd;
  Scanner scanner;

  Map<String, Function<Scanner, PhotoshopCommand>> commands = new HashMap<>();

  /**
   * Constructor to create a PhotoshopController. Runs the model.
   *
   * @param model model to run.
   * @param view  view to show program.
   */
  public APhotoshopController(PhotoshopModel model, PhotoshopView view) {
    this.model = model;
    this.view = view;
    populateCommands();
  }


  /**
   * Constructor to create a PhotoshopController. Runs the model. With a Readable.
   *
   * @param model model to run.
   * @param view  view to show program.
   * @param rd    user input.
   */
  public APhotoshopController(PhotoshopModel model, PhotoshopView view, Readable rd) {
    this(model, view);
    this.rd = rd;
    this.scanner = new Scanner(rd);
  }

  void populateCommands() {
    this.commands.put("load", (Scanner s) -> new LoadCommand(this.model, s.next(), s.next()));
    this.commands.put("save", (Scanner s) -> new SaveCommand(this.model, s.next(), s.next()));
    this.commands.put("brighten", (Scanner s) ->
            new BrightenCommand(this.model, Integer.parseInt(s.next()), s.next(), s.next()));
    this.commands.put("vertical-flip", (Scanner s) ->
            new FlipCommand(this.model, Direction.Vertical, s.next(), s.next()));
    this.commands.put("horizontal-flip", (Scanner s) ->
            new FlipCommand(this.model, Direction.Horizontal, s.next(), s.next()));
    this.commands.put("value-component", (Scanner s) ->
            new ComponentCommand(this.model, ComponentGreyscale.Value, s.next(), s.next()));
    this.commands.put("intensity-component", (Scanner s) ->
            new ComponentCommand(this.model, ComponentGreyscale.Intensity, s.next(), s.next()));
    this.commands.put("luma-component", (Scanner s) ->
            new ComponentCommand(this.model, ComponentGreyscale.Luma, s.next(), s.next()));
    this.commands.put("red-component", (Scanner s) ->
            new ComponentCommand(this.model, ComponentGreyscale.Red, s.next(), s.next()));
    this.commands.put("green-component", (Scanner s) ->
            new ComponentCommand(this.model, ComponentGreyscale.Green, s.next(), s.next()));
    this.commands.put("blue-component", (Scanner s) ->
            new ComponentCommand(this.model, ComponentGreyscale.Blue, s.next(), s.next()));
  }

  protected void runCommand(Scanner scan) {
    String next = scan.next();

    //quit.
    if (next.equalsIgnoreCase("q") || next.equalsIgnoreCase("quit")) {
      System.exit(0);
      return;
    }

    //Get command from hashmap, or return null.
    Function<Scanner, PhotoshopCommand> cmd =
            commands.getOrDefault(next, null);
    if (cmd == null) {
      throw new IllegalArgumentException("Improper command.");
    }

    //Try running command. If it doesn't work, throw IllegalArgument
    try {
      cmd.apply(scan).run();
    } catch (Exception e) {
      throw new IllegalStateException("Command failed: " + e.getMessage());
    }
  }

  @Override
  public void run() {
    while (scanner.hasNext()) {
      runCommand(scanner);
    }
  }

}
